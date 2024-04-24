// A Dagger Pipeline to for Intermission
//
// This Pipeliens steps are build with Dagger.io for the Intermission at Senacor
// It is a simple Pipeline which shows the capabilities of Dagger.io
// We try to call maven and trivy and show the output of the commdans
// We also try to retrieve a report file from pipeline step.

package main

import (
	"context"
	"fmt"
)

type Pipeline struct{}

// Returns a container that echoes whatever string argument is provided
func (m *Pipeline) ContainerEcho(stringArg string) *Container {
	return dag.Container().From("alpine:latest").WithExec([]string{"echo", stringArg})
}

// Returns a container that echoes whatever string argument is provided
func (m *Pipeline) ContainerTrivySimple(stringArg string) *Container {
	return dag.Container().From("trivy:latest").WithExec([]string{"image", stringArg})
}

func (m *Pipeline) ContainerTrivy(
	ctx context.Context,
	// + required
	imageRef string,
	// + optional
	severity string,
	// + optional
	// + default="table"
	format string,
) *Container {
	return dag.Container().From("aquasec/trivy:latest").WithExec([]string{
		"image",
		"--severity", severity,
		"--format", format,
		imageRef,
	})
}

func (m *Pipeline) ContainerTrivyStdout(
	ctx context.Context,
	// + required
	imageRef string,
	// + optional
	severity string,
	// + optional
	// + default="table"
	format string,
) (string, error) {
	return dag.Container().From("aquasec/trivy:latest").WithExec([]string{
		"image",
		"--severity", severity,
		"--format", format,
		imageRef,
	}).Stdout(ctx)
}

// Returns lines that match a pattern in the files of the provided Directory
func (m *Pipeline) GrepDir(ctx context.Context, directoryArg *Directory, pattern string) (string, error) {
	return dag.Container().
		From("alpine:latest").
		WithMountedDirectory("/mnt", directoryArg).
		WithWorkdir("/mnt").
		WithExec([]string{"grep", "-R", pattern, "."}).
		Stdout(ctx)
}

// dagger call tree --dir=. --depth=1
// dagger call tree --help                                                                                                                                                                                                              ─╯
// Usage:
//   dagger call tree [flags]

// Flags:
//
//	--depth string
//	--dir Directory
func (m *Pipeline) Tree(ctx context.Context, dir *Directory, depth string) (string, error) {
	return dag.Container().
		From("alpine:latest").
		WithMountedDirectory("/mnt", dir).
		WithWorkdir("/mnt").
		WithExec([]string{"apk", "add", "tree"}).
		WithExec([]string{"tree", "-L", depth}).
		Stdout(ctx)
}

func (m *Pipeline) MavenBuild(ctx context.Context, dir *Directory) (string, error) {
	return m.runMavenCommand(ctx, dir, "package")
}

func (m *Pipeline) MavenTest(ctx context.Context, dir *Directory) (string, error) {
	return m.runMavenCommand(ctx, dir, "test")
}

func (m *Pipeline) MavenVerify(ctx context.Context, dir *Directory) (string, error) {
	return m.runMavenCommand(ctx, dir, "clean", "verify")
}

func (m *Pipeline) runMavenCommand(ctx context.Context, dir *Directory, args ...string) (string, error) {
	mavenCache := dag.CacheVolume("maven-cache")
	return dag.Container().
		From("maven:latest").
		WithMountedCache("/root/.m2", mavenCache).
		WithMountedDirectory("/src", dir).
		WithWorkdir("/src").
		WithExec(append([]string{"mvn"}, args...)).
		Stdout(ctx)
}

// Pipeline function to retrieve a test report file from `mvn test` command.
//
// Example: dagger call maven-test-artefacts --dir=../ file --path=/usr/src/target/surefire-reports/TEST-com.senacor.intermission.carsharing.CarsharingApplicationTests.xml export --path=$(pwd)/report.xml
// The return type is a alpine container with the test report file in the /usr/src/target directory
// To retrieve help for this function, run `dagger call maven-test-artefacts --help`
func (m *Pipeline) MavenTestArtefacts(
	// Context of the pipeline
	ctx context.Context,
	// Directory to run the maven command in
	// + required
	dir *Directory) *Container {

	mavenCache := dag.CacheVolume("maven-cache")
	buildDir := dag.Container().
		From("maven:latest").
		WithMountedCache("/root/.m2", mavenCache).
		WithMountedDirectory("/src", dir).
		WithWorkdir("/src").
		WithExec([]string{"mvn", "test"}).
		Directory("/src/target")

	fileName := "TEST-com.senacor.intermission.carsharing.CarsharingApplicationTests.xmld"

	file := buildDir.File("surefire-reports/TEST-com.senacor.intermission.carsharing.CarsharingApplicationTests.xml")
	actualFileName, err := file.Name(ctx)

	if err != nil {
		fmt.Println("Failed to get file name: %v", err)
	}

	if actualFileName != fileName {
		panic("File not found")
	}

	return dag.Container().
		From("alpine:latest").
		WithDirectory("/usr/src/target", buildDir)
}
