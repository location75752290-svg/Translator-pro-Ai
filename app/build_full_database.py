# -*- coding: utf-8 -*-
import sys
import os
from generator_writer import write_kotlin_part

# We define the topics and generation logic
def generate_all():
    topics = []
    
    # 1. Greetings & Introductions
    topics.append({
        "id": 1, "name": "Greetings & Introductions", "urduName": "خیرمقدم اور تعارف", "icon": "chat",
        "desc": "Daily greetings, meeting people, and polite introductions.",
        "sentences": [
            ("Hello, how are you doing today?", "ہیلو، آپ آج کیسے ہیں؟", "Hello, aap aaj kaise hain?"),
            ("Nice to meet you.", "آپ سے مل کر بہت خوشی ہوئی۔", "Aap se mil kar bohat khushi hui."),
            ("What is your full name?", "آپ کا پورا نام کیا ہے؟", "Aap ka poora naam kya hai?"),
            ("My name is Muhammad Ali.", "میرا نام محمد علی ہے۔", "Mera naam Muhammad Ali hai."),
            ("I am doing great, thank you.", "میں بالکل ٹھیک ہوں، آپ کا شکریہ۔", "Mein bilkul theek hoon, aap ka shukriya."),
            ("Where are you originally from?", "آپ بنیادی طور پر کہاں کے رہنے والے ہیں؟", "Aap bunyadi tor par kahan ke rehne walay hain?"),
            ("I was born and raised in Lahore.", "میں لاہور میں پیدا ہوا اور وہیں پرورش پائی۔", "Mein Lahore mein paida hua aur wahin parwarish payi."),
            ("Long time no see! How have you been?", "بہت عرصے بعد ملے! آپ کا کیا حال ہے؟", "Bohat arsay baad milay! Aap ka kya haal hai?"),
            ("Have a wonderful day ahead.", "آپ کا آنے والا دن بہت اچھا گزرے۔", "Aap ka aanay wala din bohat acha guzray."),
            ("It was really great catching up with you.", "آپ سے مل کر اور بات کر کے بہت اچھا لگا۔", "Aap se mil kar aur baat kar ke bohat acha laga."),
            ("Please make yourself at home.", "برائے مہربانی اسے اپنا ہی گھر سمجھیں۔", "Baraye meharbani isay apna hi ghar samjhein."),
            ("Goodbye, take care of yourself!", "اللہ حافظ، اپنا خیال رکھیے گا!", "Allah hafiz, apna khayal rakhiye ga!"),
            ("Good morning, hope you slept well.", "صبح بخیر، امید ہے آپ کی نیند اچھی گزری ہوگی۔", "Subah bakhair, umeed hai aap ki neend achi guzri hogi."),
            ("Good evening, how was your day at work?", "شام بخیر، کام پر آپ کا دن کیسا رہا؟", "Shaam bakhair, kaam par aap ka din kaisa raha?"),
            ("Pleasure to make your acquaintance.", "آپ سے تعارف حاصل کر کے دلی مسرت ہوئی۔", "Aap se taaruf haasil kar ke dili musarrat hui."),
            ("Allow me to introduce my colleague.", "مجھے اپنے ساتھی کا تعارف کروانے کی اجازت دیں۔", "Mujhe apne saathi ka taaruf karwanay ki ijazat dein."),
            ("What do you do for a living?", "آپ کیا کام کرتے ہیں؟", "Aap kya kaam kartay hain?"),
            ("I work in IT as a software developer.", "میں آئی ٹی میں بطور سافٹ ویئر ڈویلپر کام کرتا ہوں۔", "Mein IT mein bataur software developer kaam karta hoon."),
            ("How is everything going with your studies?", "آپ کی پڑھائی کیسی چل رہی ہے؟", "Aap ki parhai kaisi chal rahi hai?"),
            ("Everything is going very well, Alhamdulillah.", "الحمدللہ سب کچھ بہت اچھا چل رہا ہے۔", "Alhamdulillah sab kuch bohat acha chal raha hai."),
            ("I would like to welcome you to Pakistan.", "میں آپ کو پاکستان میں خوش آمدید کہتا ہوں۔", "Mein aap ko Pakistan mein khush aamdeed kehta hoon."),
            ("Thank you for your warm welcome.", "آپ کے پرخلوص استقبال کا شکریہ۔", "Aap ke pur-khuloos istiqbaal ka shukriya."),
            ("Have we met somewhere before?", "کیا ہم پہلے کہیں مل چکے ہیں؟", "Kya hum pehle kahin mil chukay hain?"),
            ("Yes, we met at the wedding ceremony.", "جی ہاں، ہم شادی کی تقریب میں ملے تھے۔", "Jee haan, hum shaadi ki taqreeb mein milay thay."),
            ("Please send my regards to your parents.", "اپنے والدین کو میرا سلام کہیے گا۔", "Apne walidain ko mera salam kahiye ga."),
            ("It is an honor to meet you in person.", "آپ سے بالمشافہ ملنا میرے لیے باعث عزت ہے۔", "Aap se bil-mushafaha milna mere liye baais-e-izzat hai."),
            ("How is your whole family doing?", "آپ کے تمام اہل خانہ کیسے ہیں؟", "Aap ke tamam ahl-e-khana kaise hain?"),
            ("They are all doing very well.", "وہ سب بالکل خیر و عافیت سے ہیں۔", "Woh sab bilkul khair-o-aafiyat se hain."),
            ("Feel free to call me anytime.", "جب چاہیں بلا جھجھک مجھے فون کریں۔", "Jab chahein bila jhijhak mujhe phone karein."),
            ("I really appreciate your kind support.", "میں آپ کی مہربانی اور مدد کا بے حد مشکور ہوں۔", "Mein aap ki meharbani aur madad ka be-hadd mashkoor hoon."),
            ("Let's stay in touch through messages.", "آئیں پیغامات کے ذریعے رابطے میں رہیں۔", "Aayein paighamaat ke zariye rabtay mein rahein."),
            ("Here is my phone number and email.", "یہ میرا فون نمبر اور ای میل ایڈریس ہے۔", "Yeh mera phone number aur email address hai."),
            ("I am delighted to meet you today.", "مجھے آج آپ سے مل کر دلی خوشی ہوئی۔", "Mujhe aaj aap se mil kar dili khushi hui."),
            ("How long have you lived in this city?", "آپ اس شہر میں کتنے عرصے سے رہ رہے ہیں؟", "Aap is shehar mein kitnay arsay se reh rahe hain?"),
            ("I have lived here for nearly five years.", "میں یہاں تقریباً پانچ سال سے رہ رہا ہوں۔", "Mein yahan taqreeban paanch saal se reh raha hoon."),
            ("I hope you have a pleasant stay.", "امید ہے آپ کا قیام خوشگوار گزرے گا۔", "Umeed hai aap ka qiyam khushgawar guzray ga."),
            ("This is my younger brother, Usman.", "یہ میرا چھوٹا بھائی عثمان ہے۔", "Yeh mera chhota bhai Usman hai."),
            ("See you soon, take good care!", "جلد ملتے ہیں، اپنا اچھا خیال رکھیے گا!", "Jald miltay hain, apna acha khayal rakhiye ga!"),
            ("Good night and sleep peacefully.", "شب بخیر اور پرسکون نیند سوئیں۔", "Shab bakhair aur pursukoon neend soyein."),
            ("Thank you for your kind hospitality.", "آپ کی مہمان نوازی کا تہہ دل سے شکریہ۔", "Aap ki mehmaan nawazi ka teh-e-dil se shukriya."),
            ("I am grateful for your valuable time.", "میں آپ کے قیمتی وقت کا شکر گزار ہوں۔", "Mein aap ke qeemti waqt ka shukarguzar hoon."),
            ("Please forgive me for keeping you waiting.", "آپ کو انتظار کروانے پر معذرت خواہ ہوں۔", "Aap ko intezar karwanay par ma'azrat khwah hoon."),
            ("It is completely fine, don't worry.", "کوئی بات نہیں، بالکل فکر مت کریں۔", "Koi baat nahi, bilkul fikar mat karein."),
            ("It was a great pleasure speaking with you.", "آپ سے بات کر کے بہت خوشی ہوئی۔", "Aap se baat kar ke bohat khushi hui."),
            ("What are your plans for the weekend?", "ویک اینڈ پر آپ کا کیا پروگرام ہے؟", "Weekend par aap ka kya program hai?"),
            ("I will be spending time at home.", "میں گھر پر وقت گزاروں گا۔", "Mein ghar par waqt guzarunga."),
            ("I look forward to our next meeting.", "مجھے ہماری اگلی ملاقات کا انتظار رہے گا۔", "Mujhe hamari agli mulaqaat ka intezar rahay ga."),
            ("Please convey my congratulations to him.", "براہ کرم اسے میری طرف سے مبارکباد پہنچائیں۔", "Barah-e-karam usey meri taraf se mubarakbaad pohanchayein."),
            ("Wishing you success in all your projects.", "آپ کے تمام منصوبوں میں کامیابی کی دعا ہے۔", "Aap ke tamam mansoobon mein kamyabi ki dua hai."),
            ("Thank you very much, all the best!", "بہت بہت شکریہ، آپ کے لیے نیک تمنائیں!", "Bohat bohat shukriya, aap ke liye naik tamannayein!")
        ]
    })
    
    print("Topic 1 initialized with 50 sentences.")

generate_all()
