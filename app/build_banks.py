# -*- coding: utf-8 -*-
import sys

# Builder for topic banks
def generate_banks():
    import generate_bank_data
    generate_bank_data.create_all_banks()

if __name__ == "__main__":
    generate_banks()
