#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Direct Generator for all 50 Daily Conversation topics with 50 unique sentences per topic (2500 sentences).
"""
import os
import sys

def escape_kt(text):
    return text.replace('\\', '\\\\').replace('"', '\\"').replace('$', '\\$')

def write_part_file(file_path, class_name, topics):
    with open(file_path, "w", encoding="utf-8") as f:
        f.write("package com.example.data.model\n\n")
        f.write(f"object {class_name} {{\n")
        f.write("    val topics: List<ConversionTopic> by lazy {\n")
        f.write("        listOf(\n")
        
        for t_idx, topic in enumerate(topics):
            comma = "," if t_idx < len(topics) - 1 else ""
            f.write(f"            // {topic['id']}. {topic['name']}\n")
            f.write(f"            ConversionTopic({topic['id']}, \"{escape_kt(topic['name'])}\", \"{escape_kt(topic['urduName'])}\", \"{topic['icon']}\", \"{escape_kt(topic['desc'])}\", listOf(\n")
            
            for s_idx, s in enumerate(topic['sentences']):
                s_comma = "," if s_idx < len(topic['sentences']) - 1 else ""
                en = escape_kt(s[0])
                ur = escape_kt(s[1])
                ro = escape_kt(s[2])
                f.write(f"                ConversionSentence({s_idx+1}, \"{en}\", \"{ur}\", \"{ro}\"){s_comma}\n")
            
            f.write(f"            )){comma}\n")
            
        f.write("        )\n")
        f.write("    }\n")
        f.write("}\n")

print("Base writer ready.")
