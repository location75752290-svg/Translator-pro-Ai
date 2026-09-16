# -*- coding: utf-8 -*-
from part5_topics_41_to_45 import get_41_to_45
from part5_topics_46_to_50 import get_46_to_50
from generate_part3_full import write_part_file

topics = get_41_to_45() + get_46_to_50()
print(f"Total topics for Part 5: {len(topics)}")
for t in topics:
    print(f"Topic {t['id']} ({t['name']}): {len(t['sentences'])} sentences")
    assert len(t['sentences']) == 50

dest = "app/src/main/java/com/example/data/model/DailyConversionPart5.kt"
write_part_file(dest, "DailyConversionPart5", topics)
print(f"Successfully generated {dest}")
