"""A generated module for Intermission2023 functions

This module has been generated via dagger init and serves as a reference to
basic module structure as you get started with Dagger.

Two functions have been pre-created. You can modify, delete, or add to them,
as needed. They demonstrate usage of arguments and return types using simple
echo and grep commands. The functions can be called from the dagger CLI or
from one of the SDKs.

The first line in this comment block is a short description line and the
rest is a long description with more detail on the module's purpose or usage,
if appropriate. All modules should have a short description.
"""

import dagger
from dagger import Doc, dag, function, object_type
import logging
from typing import Annotated
import logging

from dagger.log import configure_logging

configure_logging(logging.DEBUG)

@object_type
class Intermission2023:

    @function
    def http_service(self) -> dagger.Service:
        """Starts and returns an HTTP service."""
        return (
            dag.container()
            .from_("python")
            .with_workdir("/srv")
            .with_new_file("index.html", contents="Hello, world!")
            .with_exec(["python", "-m", "http.server", "8080"])
            .with_exposed_port(8080)
            .as_service()
        )

    # dagger  call get
    @function
    async def get(self) -> str:
        """Sends a request to an HTTP service and returns the response."""
        return await (
            dag.container()
            .from_("alpine")
            .with_service_binding("www", self.http_service())
            .with_exec(["wget", "-O-", "http://www:8080"])
            .stdout()
        )

    @function
    async def hello_world(self) -> str:
        print("hello world is called")
        return "hello world"

    # export MY_TOKEN="my_token_is_here"
    # dagger call show-token --token=env:MY_TOKEN
    @function
    async def show_token(self, token: dagger.Secret) -> str:
        print("showing token")
        token_plain = await token.plaintext()
        return token_plain

    # dagger call show-error
    @function
    def show_error(self) -> str:
        print("some function is running")
        raise ValueError("this is my personal error message")
        print("this is never called")
        return "never shown"

    # dagger call mvn-verify --directory_arg=../ stdout
    @function
    async def mvn_verify(self, directory_arg: dagger.Directory) -> dagger.Container:
        maven_cache = dag.cache_volume("maven-cache")

        app = (
            dag.container()
            .from_("maven:3.9-eclipse-temurin-17")
             .with_mounted_cache("/root/.m2", maven_cache)
            .with_mounted_directory("/src", directory_arg)
            .with_workdir("/src")
        )

        build = (
            app.with_exec(["mvn",  "clean", "verify"])
        )

        return await build


    @function
    def container_echo(self, string_arg: str) -> dagger.Container:
        """Returns a container that echoes whatever string argument is provided"""
        return dag.container().from_("alpine:latest").with_exec(["echo", string_arg])

    @function
    async def grep_dir(self, directory_arg: dagger.Directory, pattern: str) -> str:
        """Returns lines that match a pattern in the files of the provided Directory"""
        return await (
            dag.container()
            .from_("alpine:latest")
            .with_mounted_directory("/mnt", directory_arg)
            .with_workdir("/mnt")
            .with_exec(["grep", "-R", pattern, "."])
            .stdout()
        )

    @function
    async def build_and_publish(
        self, build_src: dagger.Directory, build_args: list[str]
    ) -> str:
        """Build and publish a project using a Wolfi container"""
        # retrieve a new Wolfi container
        ctr = dag.wolfi().container()

        # publish the Wolfi container with the build result
        return await (

        )


    @function
    async def scan_image(
        self,
        image_ref: Annotated[
            str,
            Doc("The image reference to scan"),
        ],
        severity: Annotated[
            str,
            Doc("Severity levels to scan for"),
        ] = "UNKNOWN,LOW,MEDIUM,HIGH,CRITICAL",
        exit_code: Annotated[
            int,
            Doc("The exit code to return if vulnerabilities are found"),
        ] = 0,
        format: Annotated[
            str,
            Doc("The output format to use for the scan results"),
        ] = "table",
    ) -> str:
        """Scan the specified image for vulnerabilities."""
        return await (
            dag.container()
            .from_("aquasec/trivy:latest")
            .with_exec(
                [
                    "image",
                    "--quiet",
                    "--severity",
                    severity,
                    "--exit-code",
                    str(exit_code),
                    "--format",
                    format,
                    image_ref,
                ]
            )
            .stdout()
        )
