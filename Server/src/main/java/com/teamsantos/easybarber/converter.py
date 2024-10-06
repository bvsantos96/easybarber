import os
import re

#Run this from a folder named 'proto' in com.teamsantos.easybarber.
def camel_to_snake(name):
    pattern = re.compile(r'(?<!^)(?=[A-Z])')
    return pattern.sub('_', name).lower()

def java_type_to_proto(java_type):
    type_mapping = {
        'String': 'string',
        'int': 'int32',
        'long': 'int64',
        'float': 'float',
        'double': 'double',
        'boolean': 'bool',
        'Integer': 'int32',
        'Long': 'int64',
        'Float': 'float',
        'Double': 'double',
        'Boolean': 'bool'
    }
    return type_mapping.get(java_type, 'string')  # Default to string if type is unknown

def parse_java_file(file_path):
    with open(file_path, 'r') as file:
        content = file.read()
    
    # Extract class name
    class_match = re.search(r'class\s+(\w+)', content)
    if not class_match:
        return None, []
    
    class_name = class_match.group(1)
    
    # Extract fields
    field_pattern = r'private\s+(\w+)\s+(\w+);'
    fields = re.findall(field_pattern, content)
    
    return class_name, fields

def create_proto_file(scope, dto_folder):
    proto_content = f"""syntax = "proto3";

package com.teamsantos.easybarber.{scope};

option java_package = "com.teamsantos.easybarber.proto.{scope}";
option java_multiple_files = true;

"""
    
    for java_file in os.listdir(dto_folder):
        if java_file.endswith(".java"):
            java_path = os.path.join(dto_folder, java_file)
            class_name, fields = parse_java_file(java_path)
            
            if class_name:
                proto_content += f"message {class_name} {{\n"
                for i, (field_type, field_name) in enumerate(fields, start=1):
                    proto_type = java_type_to_proto(field_type)
                    proto_field_name = camel_to_snake(field_name)
                    proto_content += f"  {proto_type} {proto_field_name} = {i};\n"
                proto_content += "}\n\n"
    
    with open(f"{scope}.proto", 'w') as proto_file:
        proto_file.write(proto_content)

def main():
    dto_root = "../DTO"  # Adjust this path to your project structure
    for scope in os.listdir(dto_root):
        scope_path = os.path.join(dto_root, scope)
        if os.path.isdir(scope_path):
            create_proto_file(scope, scope_path)
    print("Proto files generated successfully!")

if __name__ == "__main__":
    main()