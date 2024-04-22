// A generated module for Pipeline functions
//
// This module has been generated via dagger init and serves as a reference to
// basic module structure as you get started with Dagger.
//
// Two functions have been pre-created. You can modify, delete, or add to them,
// as needed. They demonstrate usage of arguments and return types using simple
// echo and grep commands. The functions can be called from the dagger CLI or
// from one of the SDKs.
//
// The first line in this comment block is a short description line and the
// rest is a long description with more detail on the module's purpose or usage,
// if appropriate. All modules should have a short description.

package main

import (
	"context"
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
	return dag.Container().
		From("maven:latest").
		WithMountedDirectory("/src", dir).
		WithWorkdir("/src").
		WithExec([]string{"ls", "-la"}).
		WithExec([]string{"mvn", "package"}).
		Stdout(ctx)
}

func (m *Pipeline) MavenTest(ctx context.Context, dir *Directory) (string, error) {
	return dag.Container().
		From("maven:latest").
		WithMountedDirectory("/src", dir).
		WithWorkdir("/src").
		WithExec([]string{"mvn", "test"}).
		Stdout(ctx)
}

func (m *Pipeline) MavenVerify(ctx context.Context, dir *Directory) (string, error) {
	return dag.Container().
		From("maven:latest").
		WithMountedDirectory("/src", dir).
		WithWorkdir("/src").
		WithExec([]string{"mvn", "clean", "verify"}).
		Stdout(ctx)
}
