#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import sys
import os

# Complete topic generator for 50 topics x 50 sentences
from generator_writer import write_kotlin_part

def make_topics():
    # We load or generate topics with 50 rich distinct sentences each
    topics_list = []
    
    # Let's import the data generator
    import all_topics_data
    return all_topics_data.get_all_topics()

def main():
    dest_dir = "/app/src/main/java/com/example/data/model"
    topics = make_topics()
    print(f"Total topics generated: {len(topics)}")
    for t in topics:
        if len(t["sentences"]) != 50:
            print(f"ERROR: Topic {t['id']} {t['name']} has {len(t['sentences'])} sentences instead of 50!")
            sys.exit(1)
            
    # Part 1: Topics 1-10
    write_kotlin_part(os.path.join(dest_dir, "DailyTopicSentencesPart1.kt"), "DailyTopicSentencesPart1", topics[0:10])
    # Part 2: Topics 11-20
    write_kotlin_part(os.path.join(dest_dir, "DailyTopicSentencesPart2.kt"), "DailyTopicSentencesPart2", topics[10:20])
    # Part 3: Topics 21-30
    write_kotlin_part(os.path.join(dest_dir, "DailyTopicSentencesPart3.kt"), "DailyTopicSentencesPart3", topics[20:30])
    # Part 4: Topics 31-40
    write_kotlin_part(os.path.join(dest_dir, "DailyTopicSentencesPart4.kt"), "DailyTopicSentencesPart4", topics[30:40])
    # Part 5: Topics 41-50
    write_kotlin_part(os.path.join(dest_dir, "DailyTopicSentencesPart5.kt"), "DailyTopicSentencesPart5", topics[40:50])
    
    print("Successfully written all 5 parts (2,500 total unique sentences).")

if __name__ == "__main__":
    main()
