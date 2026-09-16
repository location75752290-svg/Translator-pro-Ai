# -*- coding: utf-8 -*-
import os

def escape_kotlin(s):
    return s.replace('\\', '\\\\').replace('"', '\\"').replace('$', '\\$')

# Definition of all 50 topics with 50 distinct real sentences for each topic.
# We will construct rich sentence banks for every topic.

def get_sentences_for_topic(topic_id, topic_name):
    # Specialized 50 distinct sentences per topic tailored specifically to Pakistani English learners
    # with pristine Urdu and Roman Urdu.
    return ALL_TOPIC_DATA[topic_id]

# Build ALL_TOPIC_DATA dictionary
ALL_TOPIC_DATA = {}

def add_topic(id, name, urdu_name, icon, desc, sentences):
    assert len(sentences) == 50, f"Topic {id} ({name}) has {len(sentences)} sentences instead of 50!"
    ALL_TOPIC_DATA[id] = {
        "id": id,
        "name": name,
        "urduName": urdu_name,
        "icon": icon,
        "desc": desc,
        "sentences": sentences
    }

print("Loading sentence definitions...")
