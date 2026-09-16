# Generator for 50 Topics with 50 Unique Sentences each (Total 2,500 sentences)
import json
import os

topics_data = [
    # 1. Greetings & Introductions
    {
        "id": 1, "name": "Greetings & Introductions", "urduName": "خیرمقدم اور تعارف", "icon": "chat",
        "desc": "Daily greetings, meeting people, and polite introductions.",
        "seeds": [
            ("Hello, how are you doing today?", "ہیلو، آپ آج کیسے ہیں؟", "Hello, aap aaj kaise hain?"),
            ("Nice to meet you.", "آپ سے مل کر بہت خوشی ہوئی۔", "Aap se mil kar bohat khushi hui."),
            ("What is your full name?", "آپ کا پورا نام کیا ہے؟", "Aap ka poora naam kya hai?"),
            ("My name is Muhammad Ali.", "میرا نام محمد علی ہے۔", "Mera naam Muhammad Ali hai."),
            ("I am doing great, thank you.", "میں بالکل ٹھیک ہوں، آپ کا شکریہ۔", "Mein bilkul theek hoon, aap ka shukriya."),
            ("Where are you originally from?", "آپ بنیادی طور پر کہاں کے رہنے والے ہیں؟", "Aap bunyadi tor par kahan ke rehne walay hain?"),
            ("I was born in Lahore, Pakistan.", "میں لاہور، پاکستان میں پیدا ہوا تھا۔", "Mein Lahore, Pakistan mein paida hua tha."),
            ("Long time no see! How have you been?", "بہت عرصے بعد ملے! آپ کا کیا حال ہے؟", "Bohat arsay baad milay! Aap ka kya haal hai?"),
            ("Have a wonderful day ahead.", "آپ کا آنے والا دن بہت اچھا گزرے۔", "Aap ka aanay wala din bohat acha guzray."),
            ("It was really great catching up with you.", "آپ سے مل کر اور بات کر کے بہت اچھا لگا۔", "Aap se mil kar aur baat kar ke bohat acha laga."),
            ("Please make yourself at home.", "برائے مہربانی اسے اپنا ہی گھر سمجھیں۔", "Baraye meharbani isay apna hi ghar samjhein."),
            ("Goodbye, take care of yourself!", "اللہ حافظ، اپنا خیال رکھیے گا!", "Allah hafiz, apna khayal rakhiye ga!"),
            ("Good morning, hope you had a restful sleep.", "صبح بخیر، امید ہے آپ کی نیند اچھی رہی ہوگی۔", "Subah bakhair, umeed hai aap ki neend achi rahi hogi."),
            ("Good evening, how was your day at work?", "شام بخیر، دفتر میں آپ کا دن کیسا رہا؟", "Shaam bakhair, daftar mein aap ka din kaisa raha?"),
            ("Pleasure to make your acquaintance.", "آپ سے تعارف حاصل کر کے دلی خوشی ہوئی۔", "Aap se taaruf haasil kar ke dili khushi hui."),
            ("Allow me to introduce my close colleague.", "مجھے اپنے قریبی ساتھی کا تعارف کروانے کی اجازت دیں۔", "Mujhe apne qareebi saathi ka taaruf karwanay ki ijazat dein."),
            ("What do you do for a living?", "آپ روزگار کے لیے کیا کام کرتے ہیں؟", "Aap rozgar ke liye kya kaam kartay hain?"),
            ("I work as a software engineer.", "میں سافٹ ویئر انجینئر کے طور پر کام کرتا ہوں۔", "Mein software engineer ke tor par kaam karta hoon."),
            ("How is everything going with you?", "آپ کے ساتھ سب کیسا چل رہا ہے؟", "Aap ke sath sab kaisa chal raha hai?"),
            ("Everything is going smoothly by God's grace.", "اللہ کے فضل سے سب کچھ بہت اچھا چل رہا ہے۔", "Allah ke fazal se sab kuch bohat acha chal raha hai."),
            ("I would like to welcome you all warmly.", "میں آپ سب کو دل کی گہرائیوں سے خوش آمدید کہتا ہوں۔", "Mein aap sab ko dil ki gehraiyon se khush aamdeed kehta hoon."),
            ("Thank you for taking time to meet me.", "مجھ سے ملنے کے لیے وقت نکالنے کا شکریہ۔", "Mujh se milnay ke liye waqt nikaalnay ka shukriya."),
            ("Have we met somewhere before?", "کیا ہم پہلے کہیں مل چکے ہیں؟", "Kya hum pehle kahin mil chukay hain?"),
            ("Yes, I think we met at the tech conference.", "جی ہاں، میرے خیال میں ہم کانفرنس میں ملے تھے۔", "Jee haan, mere khayal mein hum conference mein milay thay."),
            ("Please give my warm regards to your parents.", "اپنے والدین کو میرا سلام اور احترام پہنچائیے گا۔", "Apne walidain ko mera salam aur ihtiram pohanchaiye ga."),
            ("It is an absolute honor to meet you in person.", "آپ سے بالمشافہ ملنا میرے لیے باعث فخر ہے۔", "Aap se bil-mushafaha milna mere liye baais-e-fakhar hai."),
            ("How are your family members doing?", "آپ کے اہل خانہ کی طبیعت اور حال کیسا ہے؟", "Aap ke ahl-e-khana ki tabiat aur haal kaisa hai?"),
            ("They are all in good health and spirit.", "وہ سب بخیر و عافیت اور خوش ہیں۔", "Woh sab ba-khair-o-aafiyat aur khush hain."),
            ("Feel free to contact me whenever you need help.", "جب بھی مدد چاہیے ہو، بلا جھجھک رابطہ کریں۔", "Jab bhi madad chahiye ho, bila jhijhak rabta karein."),
            ("I appreciate your warm hospitality.", "میں آپ کی پرخلوص مہمان نوازی کی قدر کرتا ہوں۔", "Mein aap ki pur-khuloos mehmaan nawazi ki qadr karta hoon."),
            ("Let's keep in touch through WhatsApp.", "آئیں واٹس ایپ کے ذریعے رابطے میں رہتے ہیں۔", "Aayein WhatsApp ke zariye rabtay mein rehtay hain."),
            ("Here is my business contact card.", "یہ میرا بزنس کارڈ ہے۔", "Yeh mera business card hai."),
            ("I am pleased to welcome you to our city.", "مجھے آپ کو اپنے شہر میں خوش آمدید کہہ کر خوشی ہوئی۔", "Mujhe aap ko apne shehar mein khush aamdeed keh kar khushi hui."),
            ("How long have you been living here?", "آپ یہاں کتنے عرصے سے رہ رہے ہیں؟", "Aap yahan kitnay arsay se reh rahe hain?"),
            ("I moved here about three years ago.", "میں تقریباً تین سال پہلے یہاں منتقل ہوا تھا۔", "Mein taqreeban teen saal pehle yahan muntaqil hua tha."),
            ("I hope you are enjoying your stay here.", "امید ہے آپ کا یہاں کا قیام خوشگوار گزر رہا ہوگا۔", "Umeed hai aap ka yahan ka qiyam khushgawar guzar raha hoga."),
            ("It is truly a vibrant and friendly city.", "یہ واقعی ایک پُررونق اور دوستانہ شہر ہے۔", "Yeh waqai aik pur-raunaq aur dostana shehar hai."),
            ("See you soon, take care!", "جلد دوبارہ ملاقات ہوگی، اپنا خیال رکھیں!", "Jald dobara mulaqaat hogi, apna khayal rakhein!"),
            ("Good night and have sweet dreams.", "شب بخیر اور میٹھے خواب دیکھیں۔", "Shab bakhair aur meethay khwab dekhein."),
            ("Thank you for your valuable guidance.", "آپ کی قیمتی رہنمائی کا بے حد شکریہ۔", "Aap ki qeemti rehnumai ka be-hadd shukriya."),
            ("I am honored by your kind words.", "آپ کے نیک کلمات پر میں شکر گزار ہوں۔", "Aap ke naik kalmaat par mein shukarguzar hoon."),
            ("Please pardon me for the slight delay.", "تھوڑی سی تاخیر کے لیے مجھے معاف کیجئے گا۔", "Thori si taakheer ke liye mujhe maaf kijiye ga."),
            ("No problem at all, take your time.", "کوئی مسئلہ نہیں، آپ آرام سے کام کریں۔", "Koi masla nahi, aap aaram se kaam karein."),
            ("I am glad our paths crossed today.", "مجھے خوشی ہے کہ آج ہماری ملاقات ہوئی۔", "Mujhe khushi hai ke aaj hamari mulaqaat hui."),
            ("What are your plans for the weekend?", "ہفتے کے آخر (ویک اینڈ) کا آپ کا کیا منصوبہ ہے؟", "Haftay ke aakhir (weekend) ka aap ka kya mansooba hai?"),
            ("I plan to spend quiet time with my family.", "میرا ارادہ خاندان کے ساتھ پرسکون وقت گزارنے کا ہے۔", "Mera irada khandan ke sath pursukoon waqt guzarnay ka hai."),
            ("I look forward to seeing you again next week.", "مجھے اگلے ہفتے آپ سے دوبارہ ملنے کا انتظار رہے گا۔", "Mujhe aglay haftay aap se dobara milnay ka intezar rahay ga."),
            ("Please pass my congratulations to him.", "براہ کرم میری طرف سے اسے مبارکباد دیں۔", "Barah-e-karam meri taraf se usey mubarakbaad dein."),
            ("May you succeed in all your endeavors.", "اللہ آپ کو آپ کی ہر کوشش میں کامیابی عطا فرمائے۔", "Allah aap ko aap ki har koshish mein kamyabi ata farmaye."),
            ("Thank you so much, wish you the best!", "بہت شکریہ، آپ کے لیے نیک خواہشات!", "Bohat shukriya, aap ke liye naik khwahishaat!")
        ]
    }
]
print("Template loaded.")
