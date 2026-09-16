# -*- coding: utf-8 -*-
import sys

# Define topics 21 to 30
from part3_data import get_topics_21_to_30
from generate_part3_full import write_part_file

topics = get_topics_21_to_30()
print(f"Total topics for Part 3: {len(topics)}")
for t in topics:
    print(f"Topic {t['id']} ({t['name']}): {len(t['sentences'])} sentences")
    assert len(t['sentences']) == 50

dest = "app/src/main/java/com/example/data/model/DailyConversionPart3.kt"
write_part_file(dest, "DailyConversionPart3", topics)
print(f"Successfully generated {dest}")
