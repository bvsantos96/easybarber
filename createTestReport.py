import os
from pathlib import Path
from xml.etree import ElementTree as ET
import json
import ast

def generate_github_comment(test_results):
    """
    Generate a GitHub comment-style formatted string for test results.
    
    Args:
        test_results (list): List of test result dictionaries
    
    Returns:
        str: Formatted GitHub comment string
    """
    passed_tests = []
    failed_tests = []
    
    for test_suite in test_results:
        if test_suite['nFailures'] == 0 and test_suite['nErrors'] == 0:
            passed_tests.append(test_suite)
        else:
            failed_tests.append(test_suite)
    
    comment = []
    
    if passed_tests:
        total_passed_tests = sum(suite['nTests'] for suite in passed_tests)
        comment.append(f"<details>\n<summary>Tests passed (<span style=\"color: green;\">{total_passed_tests}</span>)</summary>\n\n")
        
        for suite in passed_tests:
            comment.append(f"  <details style=\"margin-left: 20px; font-size: 14px\"> \n  <summary>{suite['name']} (time: {suite['time']}, nTests: {suite['nTests']}, nErrors: {suite['nErrors']}, nFailures: {suite['nFailures']})</summary>\n\n")
            
            for test in suite['tests']:
                comment.append(f"  - ✅ **{test['name']}** (time: {test['time']})")
            
            comment.append("</details>")
        comment.append("</details>\n")
    
    if failed_tests:
        total_failed_tests = sum(suite['nFailures'] + suite['nErrors'] for suite in failed_tests)
        comment.append(f"\n<details>\n<summary>Tests Failed (<span style=\"color: red;\">{total_failed_tests}</span>)</summary>\n\n")
        
        for suite in failed_tests:
            comment.append(f"  <details style=\"margin-left: 20px; font-size: 14px\"> \n  <summary>{suite['name']} (time: {suite['time']}, nErrors: {suite['nErrors']}, nFailures: {suite['nFailures']})</summary>\n\n")
            
            for test in suite['tests']:
                if test['failure'] is not None:
                    comment.append(f"  - ❌ {test['name']} (time: {test['time']})")
                    comment.append(f"  ````java\n  {test['failure']['details']}\n  ````")
            
            comment.append("</details>")
        comment.append("</details>\n")
    
    return "\n".join(comment)

def parse_test_report(file_path):
    """
    Parse a single XML file and extract test suite and test case information.
    """
    tree = ET.parse(file_path)
    root = tree.getroot()

    testsuite_attributes = root.attrib

    testsuite_data = {
        "name": testsuite_attributes.get("name", ""),
        "time": float(testsuite_attributes.get("time", 0)),
        "nTests": int(testsuite_attributes.get("tests", 0)),
        "nErrors": int(testsuite_attributes.get("errors", 0)),
        "nSkipped": int(testsuite_attributes.get("skipped", 0)),
        "nFailures": int(testsuite_attributes.get("failures", 0)),
        "tests": [],
    }

    for testcase in root.findall("testcase"):
        testcase_data = {
            "name": testcase.attrib.get("name", ""),
            "time": float(testcase.attrib.get("time", 0)),
            "failure": None,
        }

        failure = testcase.find("failure")
        if failure is not None:
            testcase_data["failure"] = {
                "type": failure.attrib.get("type", ""),
                "message": failure.attrib.get("message", ""),
                "details": failure.text.strip() if failure.text else "",
            }

        testsuite_data["tests"].append(testcase_data)

    return testsuite_data

def parse_all_test_reports(directory_path):
    """
    Parse all .xml files in the given directory and return a list of test suite data.
    """
    test_reports = []
    directory = Path(directory_path)

    for xml_file in directory.glob("*.xml"):
        test_reports.append(parse_test_report(xml_file))

    return test_reports

reports_directory = "Server/target/surefire-reports/"

if __name__ == "__main__":

    if not os.path.exists(reports_directory):
        print(f"Directory {reports_directory} does not exist. Please check the path.")
    else:
        reports = parse_all_test_reports(reports_directory)

        github_comment = generate_github_comment(reports)

        with open("report.md", "w") as file:
            file.write(github_comment)
