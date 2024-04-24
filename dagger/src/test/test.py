import unittest
import sys
import os
import subprocess


class TestDagger(unittest.TestCase):

    def test_hello_world(self):
        exit_status = os.system('dagger call hello-world')
        assert exit_status == 0

    def test_error(self):
        exit_status = os.system('dagger call show-error')
        assert exit_status != 0

    def test_verify_jacoco_was_executed(self):
        exit_status = os.system('dagger call mvn-verify --directory_arg=../../../ file --path=/src/target/site/jacoco/jacoco.csv name')
        assert exit_status == 0

    def test_verify_jacoco_was_executed_report_matches_name(self):
        proc = subprocess.Popen(["dagger call mvn-verify --directory_arg=../../../ file --path=/src/target/site/jacoco/jacoco.csv name"], stdout=subprocess.PIPE, shell=True)
        (out, err) = proc.communicate()
        assert out.decode('utf-8') == "jacoco.csv"
        assert err == None

    def test_verify_jacoco_was_executed_report_greater_than_10byte(self):
        proc = subprocess.Popen(["dagger call mvn-verify --directory_arg=../../../ file --path=/src/target/site/jacoco/jacoco.csv contents"], stdout=subprocess.PIPE, shell=True)
        (out, err) = proc.communicate()
        assert sys.getsizeof(out) > 10
        assert err == None
