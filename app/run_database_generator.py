#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import sys
import os

# Sentence Bank Data for all 50 topics
# Each topic has 50 tailored sentences
import topic_bank_part1
import topic_bank_part2
import topic_bank_part3
import topic_bank_part4
import topic_bank_part5

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

def main():
    dest_dir = "/app/src/main/java/com/example/data/model"
    
    t_p1 = topic_bank_part1.get_topics()
    t_p2 = topic_bank_part2.get_topics()
    t_p3 = topic_bank_part3.get_topics()
    t_p4 = topic_bank_part4.get_topics()
    t_p5 = topic_bank_part5.get_topics()
    
    print(f"P1: {len(t_p1)}, P2: {len(t_p2)}, P3: {len(t_p3)}, P4: {len(t_p4)}, P5: {len(t_p5)}")
    
    write_part_file(os.path.join(dest_dir, "DailyTopicSentencesPart1.kt"), "DailyTopicSentencesPart1", t_p1)
    write_part_file(os.path.join(dest_dir, "DailyTopicSentencesPart2.kt"), "DailyTopicSentencesPart2", t_p2)
    write_part_file(os.path.join(dest_dir, "DailyTopicSentencesPart3.kt"), "DailyTopicSentencesPart3", t_p3)
    write_part_file(os.path.join(dest_dir, "DailyTopicSentencesPart4.kt"), "DailyTopicSentencesPart4", t_p4)
    write_part_file(os.path.join(dest_dir, "DailyTopicSentencesPart5.kt"), "DailyTopicSentencesPart5", t_p5)
    
    print("All 5 parts generated successfully.")

if __name__ == "__main__":
    main()
