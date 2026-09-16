# -*- coding: utf-8 -*-
from part4_topics_31_to_35 import get_31_to_35
from part4_topics_36_to_40 import get_36_to_40
from generate_part3_full import write_part_file

topics = get_31_to_35() + get_36_to_40()
print(f"Total topics for Part 4: {len(topics)}")
for t in topics:
    print(f"Topic {t['id']} ({t['name']}): {len(t['sentences'])} sentences")
    assert len(t['sentences']) == 50

dest = "app/src/main/java/com/example/data/model/DailyConversionPart4.kt"
write_part_file(dest, "DailyConversionPart4", topics)
print(f"Successfully generated {dest}")
