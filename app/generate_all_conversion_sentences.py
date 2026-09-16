#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script to generate 50 Topics with 50 unique sentences each (Total 2,500 sentences)
Saved cleanly across 5 Kotlin files:
- DailyTopicSentencesPart1.kt (Topics 1-10)
- DailyTopicSentencesPart2.kt (Topics 11-20)
- DailyTopicSentencesPart3.kt (Topics 21-30)
- DailyTopicSentencesPart4.kt (Topics 31-40)
- DailyTopicSentencesPart5.kt (Topics 41-50)
"""

import sys
import os

# Data generator dictionary
def escape_kotlin(s):
    return s.replace('\\', '\\\\').replace('"', '\\"').replace('$', '\\$')

def main():
    dest_dir = "/app/src/main/java/com/example/data/model"
    os.makedirs(dest_dir, exist_ok=True)
    
    # We will generate comprehensive topics
    import topic_sentence_bank
    topic_sentence_bank.generate_kotlin_files(dest_dir)

if __name__ == "__main__":
    main()
