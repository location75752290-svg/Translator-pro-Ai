# -*- coding: utf-8 -*-
# Complete dataset of 50 topics, each with 50 distinct real-world conversational sentences
import json

def get_all_topics():
    # Load or generate all 50 topics
    import topic_definitions_full
    return topic_definitions_full.TOPICS
