# -*- coding: utf-8 -*-

def add_26_to_30(topics):
    # 26. Music & Arts
    t26 = [
        ("Music speaks a universal language that touches every human soul.", "موسیقی ایک ایسی عالمگیر زبان ہے جو ہر روح کو چھو لیتی ہے۔", "Music aik aisi aalamgeer zaban hai jo har rooh ko chhoo leti hai."),
        ("Have you listened to classical Eastern Qawwali performances?", "کیا آپ نے روایتی مشرقی قوالی کی محفلیں سنی ہیں؟", "Kya aap ne riwayati mashriqi qawwali ki mehfilein suni hain?"),
        ("The harmonium and tabla create a mesmerizing musical rhythm.", "ہارمونیم اور طبلہ ایک سحر انگیز لے اور تال پیدا کرتے ہیں۔", "Harmonium aur tabla aik sehar-angeez lay aur taal paida kartay hain."),
        ("He is a gifted painter specializing in oil portrait art.", "وہ آئل پورٹریٹ آرٹ میں غیر معمولی مہارت رکھنے والا مصور ہے۔", "Woh oil portrait art mein ghair mamooli maharat rakhnay wala musawir hai."),
        ("The local art exhibition showcased contemporary Pakistani sculptures.", "مقامی نمائش میں جدید پاکستانی مجسمہ سازی کی نمائش کی گئی۔", "Maqami numaish mein jadeed Pakistani mujasma saazi ki numaish ki gayi."),
        ("Learning to play the violin requires immense discipline and ear training.", "وائلن بجانا سیکھنے کے لیے سخت محنت اور سروں کی پہچان چاہیے۔", "Violin bajana seekhnay ke liye sakht mehnat aur suron ki pehchan chahiye."),
        ("Traditional hand-woven carpets are true masterpieces of craft.", "ہاتھ سے بنے روایتی قالین دستکاری کا اعلیٰ شاہکار ہیں۔", "Haath se banay riwayati qaleen dastkari ka aala shahkaar hain."),
        ("Her melodious voice echoed beautifully throughout the auditorium.", "اس کی سریلی آواز پورے آڈیٹوریم میں خوبصورتی سے گونج اٹھی۔", "Us ki sureeli aawaz pooray auditorium mein khubsurti se goonj uthi."),
        ("Calligraphic art with verses of wisdom adorns many home walls.", "حکمت سے بھرپور اشعار کی خطاطی گھروں کی دیواروں کو سجاتی ہے۔", "Hikmat se bharpoor ashaar ki khattati gharon ki deewaron ko sajati hai."),
        ("Art allows individuals to express what words fail to capture.", "آرٹ انسان کو وہ جذبات ظاہر کرنے دیتا ہے جو الفاظ نہیں کہہ پاتے۔", "Art insaan ko woh jazbaat zahir karne deta hai jo alfaaz nahi keh paatay."),
        ("The rhythm of the flute brings a sense of serenity to the heart.", "بانسری کی دھن دل کو ایک انوکھا سکون اور قرار بخشتی ہے۔", "Bansuri ki dhun dil ko aik anokha sukoon aur qaraar bakhshti hai."),
        ("Do you prefer acoustic unplugged music or electronic beats?", "کیا آپ قدرتی سازوں والی موسیقی پسند کرتے ہیں یا الیکٹرانک؟", "Kya aap qudrati saazon wali music pasand kartay hain ya electronic?"),
        ("The art gallery features works by emerging national painters.", "آرٹ گیلری میں نئے ابھرتے ہوئے ملکی مصوروں کے فن پارے ہیں۔", "Art gallery mein naye ubhartay hue mulki musawiron ke fann paaray hain."),
        ("Pottery crafting with wet river clay is an ancient tradition.", "دریائی مٹی سے برتن بنانا ایک صدیوں پرانی خوبصورت روایت ہے۔", "Daryai mitti se bartan banana aik sadiyon purani khubsurat riwayat hai."),
        ("The symphony orchestra played a magnificent Beethoven concerto.", "سمفنی آرکسٹرا نے بیتھوون کا شاندار کلاسک نغمہ پیش کیا۔", "Symphony orchestra ne Beethoven ka shandar classic naghma pesh kiya."),
        ("He mastered the complex raags of classical Hindustani music.", "اس نے کلاسیکی موسیقی کے پیچیدہ راگوں پر مکمل مہارت حاصل کی۔", "Us ne classical music ke paicheeda raagon par mukammal maharat haasil ki."),
        ("Geometric mosaic patterns on ancient mosques are awe-inspiring.", "قدیم مساجد پر ہندسی ٹائلوں کے نقوش عقل کو دنگ کر دیتے ہیں۔", "Qadeem masajid par handasi tiles ke nuqoosh aql ko dang kar detay hain."),
        ("Writing lyrical poetry requires acute sensitivity to life.", "نغمگی والی شاعری لکھنے کے لیے زندگی کی گہری سمجھ چاہیے۔", "Naghmagi wali shayari likhnay ke liye zindagi ki gehri samajh chahiye."),
        ("She held her first solo watercolor painting exhibition today.", "آج اس کی پانی کے رنگوں والی مصوری کی پہلی سولو نمائش ہوئی۔", "Aaj us ki water color painting ki pehli solo numaish hui."),
        ("Folk music preserves the cultural heritage of rural villages.", "لوک موسیقی دیہی علاقوں کے ثقافتی ورثے کو زندہ رکھتی ہے۔", "Lok music deehi ilaaqon ke saqafati virsay ko zinda rakhti hai."),
        ("The beat of the Punjabi dhol energizes everyone at celebrations.", "پنجابی ڈھول کی تھاپ تقریب میں موجود ہر شخص کو جھومنے پر مجبور کرتی ہے۔", "Punjabi dhol ki thaap taqreeb mein maujood har shakhs ko jhoomnay par majboor karti hai."),
        ("Art therapy helps individuals process emotional traumas gently.", "آرٹ تھراپی انسان کو جذباتی زخموں سے پرسکون طریقے سے نکالتی ہے۔", "Art therapy insaan ko jazbaati zakhmon se pursukoon tareeqay se nikaalti hai."),
        ("The sculptor spent five years carving this marble statue.", "مجسمہ ساز نے اس سنگ مرمر کے مجسمے کو تراشنے میں پانچ سال لگائے۔", "Mujasma saaz ne is sang-e-marmar ke mujasmay ko tarashnay mein paanch saal lagaye."),
        ("Classical music sharpens mathematical thinking in growing children.", "کلاسیکی موسیقی بڑھتے ہوئے بچوں کی ریاضیاتی سوچ کو تیز کرتی ہے۔", "Classical music barhtay hue bachon ki riyaziyati soch ko taiz karti hai."),
        ("Every brushstroke conveys emotion, light, shadow, and depth.", "برش کا ہر اسٹروک جذبہ، روشنی، سایہ اور گہرائی بیان کرتا ہے۔", "Brush ka har stroke jazba, roshni, saya aur gehrai bayan karta hai."),
        ("The Rubab is a beloved traditional stringed instrument in the north.", "شمالی علاقوں میں رباب ایک انتہائی محبوب روایتی تاروں والا ساز ہے۔", "Shumali ilaaqon mein rubab aik intehai mehboob riwayati saaz hai."),
        ("Creativity flourishes when the mind is granted true freedom.", "جب ذہن کو سچی آزادی ملتی ہے تو تخلیقی صلاحیتیں نکھرتی ہیں۔", "Jab zehan ko sachi azaadi milti hai to takhleeqi salahiyatein nikharti hain."),
        ("The cultural festival celebrated rich folk dances and poetry.", "ثقافتی میلے میں روایتی رقص اور شاعری کی محفلیں سجائی گئیں۔", "Saqafati meelay mein riwayati raqs aur shayari ki mehfilein sajayi gayeen."),
        ("His lyrics capture the longing of wandering human hearts.", "اس کے گیتوں کے بول بھٹکتے ہوئے دلوں کی تڑپ کو بیان کرتے ہیں۔", "Us ke geeton ke bol bhataktay hue dilon ki tarap ko bayan kartay hain."),
        ("A life enriched by art and melody is deeply fulfilled.", "فن اور نغموں سے سجی زندگی حقیقی معنوں میں پرمسرت ہوتی ہے۔", "Fann aur naghmon se saji zindagi haqeeqi maanon mein pur-musarrat hoti hai."),
        ("The singer received thunderous applause from the crowd.", "گلوکار کو محفل میں موجود سامعین کی طرف سے زبردست داد ملی۔", "Gulukaar ko mehfil mein maujood saami'een ki taraf se zabardast daad mili."),
        ("Traditional brass metalwork requires intricate engraving skills.", "پیتل کے برتنوں پر نقش و نگار بنانے کے لیے باریک کاریگری چاہیے۔", "Peetal ke bartanon par naqsh-o-nigaar bananay ke liye bareek kaareegari chahiye."),
        ("Art galleries offer a serene sanctuary away from city rush.", "آرٹ گیلریاں شہر کے شور و غل سے دور ایک پرسکون پناہ گاہ ہیں۔", "Art galleries shehar ke shor-o-ghul se door aik pursukoon panah-gah hain."),
        ("The guitar solo during the acoustic track was soulful.", "گٹار کا اکیلا نغمہ روح کی گہرائیوں میں اتر جانے والا تھا۔", "Guitar ka akela naghma rooh ki gehraiyon mein utar jaanay wala tha."),
        ("Visual storytelling transcends barriers of spoken languages.", "تصویری کہانیاں زبانی زبانوں کی تمام رکاوٹوں کو مٹا دیتی ہیں۔", "Tasveeri kahaniyan zabani zabanon ki tamam rukawaton ko mita deti hain."),
        ("He composes devotional hymns that bring tears to listeners.", "وہ ایسے روحانی کلام ترتیب دیتا ہے جو سننے والوں کو رلا دیتے ہیں۔", "Woh aisay roohani kalaam tarteeb deta hai jo sunnay walon ko rula detay hain."),
        ("Colors on the canvas mirror the artist's deepest feelings.", "کینوس پر رنگ مصور کے گہرے ترین جذبات کا عکس پیش کرتے ہیں۔", "Canvas par rang musawir ke gehray tareen jazbaat ka aks pesh kartay hain."),
        ("She teaches traditional pottery to students on Saturdays.", "وہ ہفتے کے دن طلبہ کو روایتی برتن سازی سکھاتی ہے۔", "Woh haftay ke din talba ko riwayati bartan-saazi sikhati hai."),
        ("The musical concert raised generous funds for flood relief.", "موسیقی کے کنسرٹ سے سیلاب زدگان کے لیے دل کھول کر فنڈز اکٹھے کیے گئے۔", "Music ke concert se sailaab zadgaan ke liye funds ikatthay kiye gaye."),
        ("Authentic craftsmanship carries the living soul of our ancestors.", "اصلی دستکاری میں ہمارے بزرگوں کی زندہ روح دھڑکتی ہے۔", "Asli dastkari mein hamare bazurgon ki zinda rooh dharrakti hai."),
        ("The blend of modern rock and classical eastern sitar is unique.", "جدید راک اور روایتی ستار کا امتزاج انتہائی منفرد ہے۔", "Jadeed rock aur riwayati sitar ka imtizaj intehai munfarid hai."),
        ("Street murals and graffiti have brightened the urban walls.", "شہری دیواروں پر بنی خوبصورت پینٹنگز نے شہر کو رنگین بنا دیا۔", "Shehri deewaron par bani khubsurat paintings ne shehar ko rangeen bana diya."),
        ("Listening to uplifting spiritual melodies elevates personal mood.", "روحانی نغمے سننا انسان کے موڈ کو خوشگوار بنا دیتا ہے۔", "Roohani naghmay sunna insaan ke mood ko khushgawar bana deta hai."),
        ("The art curator explained the historical context of each piece.", "آرٹ کیوریٹر نے ہر فن پارے کا تاریخی پس منظر سمجھایا۔", "Art curator ne har fann paaray ka tareekhi pas-manzar samjhaya."),
        ("Learning to sketch with charcoal trains your perception.", "کوئلے سے اسکیچ بنانا انسان کے مشاہدے کی صلاحیت کو نکھارتا ہے۔", "Koilay se sketch banana insaan ke mushahiday ko nikharta hai."),
        ("Music brings estranged communities together in harmony.", "موسیقی دور ہوئے معاشروں کو محبت کے ساتھ ایک لڑی میں پروتی ہے۔", "Music door hue ma'ashron ko mohabbat ke sath aik larry mein piroti hai."),
        ("Every culture has its own distinctive artistic legacy.", "ہر تہذیب کا اپنا ایک الگ اور منفرد فنی و ثقافتی ورثہ ہے۔", "Har tehzeeb ka apna aik alag aur munfarid saqafati virsa hai."),
        ("Art teaches us to see profound beauty in ordinary daily life.", "فن ہمیں روزمرہ کی عام چیزوں میں بھی گہری خوبصورتی دیکھنا سکھاتا ہے۔", "Fann humein rozmarrah ki aam cheezon mein bhi gehri khubsurti dekhna sikhata hai."),
        ("Cherish the creative talents of poets, singers, and artisans.", "شاعروں، گلوکاروں اور کاریگروں کی تخلیقی صلاحیتوں کی قدر کریں۔", "Shairon, gulukaron aur kareegaron ki takhleeqi salahiyaton ki qadr karein."),
        ("May your journey be accompanied by beauty, harmony, and song!", "آپ کا سفر ہمیشہ خوبصورتی، ہم آہنگی اور سروں سے سجا رہے!", "Aap ka safar hamesha khubsurti aur suron se saja rahay!")
    ]
    topics.append({
        "id": 26, "name": "Music & Arts", "urduName": "موسیقی اور فنون", "icon": "favorite",
        "desc": "Traditional instruments, singing, classical music, and exhibitions.", "sentences": t26
    })

    # 27. Clothes & Fashion
    t27 = [
        ("What type of fabric is best suited for hot summer days?", "گرمی کے موسم کے لیے کون سا کپڑا سب سے بہترین رہتا ہے؟", "Garmi ke mausam ke liye kaun sa kapra sab se behtareen rehta hai?"),
        ("Pure breathable cotton keeps you cool and comfortable in heat.", "خالص سوتی کپڑا گرمی میں آپ کو ٹھنڈا اور آرام دہ رکھتا ہے۔", "Khalis sooti kapra garmi mein aap ko thanda aur aaram deh rakhta hai."),
        ("This embroidered traditional Shalwar Kameez looks majestic.", "یہ کڑھائی والا روایتی شلوار قمیض بہت شاندار لگ رہا ہے۔", "Yeh karhai wala riwayati shalwar kameez bohat shandar lag raha hai."),
        ("Does this formal tailored suit match with black leather shoes?", "کیا یہ سلائی شدہ سوٹ کالے چمڑے کے جوتوں کے ساتھ جچتا ہے؟", "Kya yeh silai shuda suit kaalay chamrray ke jooton ke sath jachta hai?"),
        ("I need to get these trousers hemmed by a master tailor.", "مجھے یہ پتلون درزی سے نیچے سے چھوٹی کروانی ہے۔", "Mujhe yeh patloon darzi se neechay se chhoti karwani hai."),
        ("Pastel colors are very fashionable during spring and summer.", "بہار اور گرمیوں میں ہلکے رنگ (پاسٹل کلرز) فیشن میں رہتے ہیں۔", "Bahar aur garmiyon mein halkay rang fashion mein rehtay hain."),
        ("Always wash delicate silk garments with mild laundry detergent.", "ریشمی کپڑوں کو ہمیشہ ہلکے اور نرم صابن سے دھوئیں ہے۔", "Reshmi kapron ko hamesha halkay aur narm saban se dhoein."),
        ("A well-fitted leather jacket adds elegance to winter style.", "اچھی فٹنگ والی چمڑے کی جیکٹ سردیوں کے انداز کو پروقار بناتی ہے۔", "Achi fitting wali chamrray ki jacket sardiyon ke andaaz ko pur-waqaar banati hai."),
        ("Simplicity in dressing reflects genuine class and sophistication.", "لباس میں سادگی ہی انسان کے اعلیٰ ذوق اور وقار کا پتہ دیتی ہے۔", "Libaas mein saadgi hi insaan ke aala zauq aur waqaar ka pata deti hai."),
        ("Do you prefer buying ready-to-wear or custom-tailored clothes?", "کیا آپ تیار شدہ کپڑے پسند کرتے ہیں یا خود سلوانا؟", "Kya aap tayar shuda kapray pasand kartay hain ya khud silwana?"),
        ("This matching silk dupatta completes the festive wedding outfit.", "یہ میچنگ ریشمی دوپٹہ شادی کے لباس کو مکمل اور پررونق بناتا ہے۔", "Yeh matching reshmi dupatta shaadi ke libaas ko mukammal banata hai."),
        ("Iron your dress shirts with steam for a crisp, wrinkle-free look.", "قمیضوں کو شکنوں سے پاک کرنے کے لیے بھاپ والی استری کریں۔", "Qameezon ko shikanon se paak karne ke liye bhaap wali istri karein."),
        ("Wear warm woolen thermal layers to protect against harsh winds.", "سرد ہواؤں سے بچنے کے لیے اندر گرم تھرمل ضرور پہنیں۔", "Sard hawaon se bachnay ke liye andar garam thermal zaroor pehnein."),
        ("Dark navy and charcoal suits are standard for corporate meetings.", "دفتری میٹنگز کے لیے نیوی بلیو اور سرمئی سوٹ معیاری سمجھے جاتے ہیں۔", "Daftari meetings ke liye navy blue aur surmayi suit meyaari samjhay jaatay hain."),
        ("Where can I find premium Boski and Karandi fabric in Lahore?", "لاہور میں بوسکی اور کرنڈی کا بہترین کپڑا کہاں سے ملتا ہے؟", "Lahore mein Boski aur Karandi ka behtareen kapra kahan se milta hai?"),
        ("These hand-embroidered Peshawari chappals are very sturdy.", "یہ ہاتھ سے سلی ہوئی پشاوری چپلیں بہت پائیدار اور خوبصورت ہیں۔", "Yeh haath se sili hui Peshawari chappalein bohat payedaar hain."),
        ("Make sure the collar button is fastened comfortably around neck.", "دیکھیں کہ کالر کا بٹن گلے پر آرام سے بند ہو رہا ہو۔", "Dekhein ke collar ka button galay par aaram se band ho raha ho."),
        ("Neutral shades like beige and olive blend gracefully with anything.", "ہلکے بادامی اور زیتونی رنگ ہر لباس کے ساتھ اچھے لگتے ہیں۔", "Halkay baadami aur zaitooni rang har libaas ke sath achay lagtay hain."),
        ("She designed an exclusive bridal couture collection for fashion week.", "اس نے فیشن ویک کے لیے دلہنوں کے ملبوسات کا نیا مجموعہ تیار کیا۔", "Us ne fashion week ke liye dulhanon ke malboosaat ka naya majmooa tayar kiya."),
        ("Comfort should always be your top priority in daily fashion.", "روزمرہ کے فیشن میں آرام دہ ہونا ہمیشہ اولین ترجیح ہونا چاہیے۔", "Rozmarrah ke fashion mein aaram deh hona hamesha awaleen tarjeeh hona chahiye."),
        ("Can you replace the broken zipper on this winter parka?", "کیا آپ اس ونٹر پارک جیکٹ کا ٹوٹا ہوا زپ تبدیل کر سکتے ہیں؟", "Kya aap is winter parka jacket ka toota hua zip tabdeel kar saktay hain?"),
        ("Khadi fabric has an authentic rustic charm and earthy feel.", "کھدر کے کپڑے میں ایک دیسی اور خالص روایتی کشش ہوتی ہے۔", "Khaddar ke kapray mein aik desi aur khalis riwayati kashish hoti hai."),
        ("A neat leather wristwatch complements any formal attire.", "چمڑے کے پٹے والی صاف ستھری گھڑی ہر سوٹ کے ساتھ خوب جچتی ہے۔", "Chamrray ke pattay wali saaf suthri gharri har suit ke sath khoob jachti hai."),
        ("Wash dark colors separately to avoid accidental color bleeding.", "رنگ اترنے سے بچنے کے لیے گہرے رنگ کے کپڑے الگ دھوئیں۔", "Rang utarnay se bachnay ke liye gehray rang ke kapray alag dhoein."),
        ("Traditional Ajrak shawls represent timeless Sindhi heritage.", "روایتی اجرک کی چادریں سندھ کے لازوال ثقافتی ورثے کی عکاسی کرتی ہیں۔", "Riwayati Ajrak ki chaadrein Sindh ke la-zawal saqafati virsay ki akkasi karti hain."),
        ("This waist belt is made of 100% genuine full-grain leather.", "یہ بیلٹ سو فیصد اصلی اور معیاری چمڑے سے تیار کی گئی ہے۔", "Yeh belt sau feesad asli aur meyaari chamrray se tayar ki gayi hai."),
        ("Polishing your formal shoes daily keeps the leather supple.", "روزانہ جوتوں کو پالش کرنا چمڑے کو نرم اور چمکدار رکھتا ہے۔", "Rozana jooton ko polish karna chamrray ko narm aur chamakdaar rakhta hai."),
        ("He wore a classic black Sherwani for his wedding reception.", "اس نے ولیمے کی تقریب پر کلاسک کالی شیروانی زیب تن کی۔", "Us ne walimay ki taqreeb par classic kaali sherwani zaib-e-tan ki."),
        ("Avoid fast fashion trends; invest in durable timeless pieces.", "عارضی فیشن کے بجائے ہمیشہ پائیدار اور کلاسیک لباس خریدیں۔", "Aarzi fashion ke bajaye hamesha payedaar aur classic libaas khareedein."),
        ("Good posture makes any outfit look tenfold more distinguished.", "سیدھے کھڑے ہونے کا سلیقہ ہر لباس کو دس گنا زیادہ باوقار بناتا ہے۔", "Seedhay kharray honay ka saleeqa har libaas ko das guna zyada ba-waqaar banata hai."),
        ("Can I get this cuff altered to fit my wrist properly?", "کیا یہ کف میری کلائی کے سائز کے مطابق ٹھیک ہو سکتا ہے؟", "Kya yeh cuff meri kalai ke size ke mutabiq theek ho sakta hai?"),
        ("This woolen shawl provides warmth during freezing hill station trips.", "یہ اون کی شال مری اور شمالی علاقوں کی شدید سردی میں حرارت دیتی ہے۔", "Yeh oon ki shawl Murree aur shumali ilaaqon ki shadeed sardi mein hararat deti hai."),
        ("Choose shoes that provide excellent arch support for walking.", "ایسے جوتے منتخب کریں جو چلنے کے دوران پاؤں کو مکمل سہارا دیں۔", "Aisay jootay muntakhib karein jo chalnay ke dauran paon ko sahara dein."),
        ("Subtle perfume applied to pulse points lasts all day long.", "کلائی اور گردن پر ہلکا پرفیوم لگانا سارا دن مہکتا رہتا ہے۔", "Kalai aur gardan par halka perfume lagana sara din mehakta rehta hai."),
        ("The tailors in Anarkali bazaar are renowned for bridal embroidery.", "انارکلی بازار کے درزی دلہن کی کڑھائی کے لیے مشہور ہیں۔", "Anarkali bazaar ke darzi dulhan ki karhai ke liye mashhoor hain."),
        ("A navy blazer paired with khaki trousers is a timeless look.", "نیوی بلیو بلیزر کے ساتھ خاکی پتلون ہمیشہ فیشن میں رہتی ہے۔", "Navy blue blazer ke sath khaki patloon hamesha fashion mein rehti hai."),
        ("Hang your heavy winter coats on sturdy wooden hangers.", "سردیوں کے وزنی کوٹ لکڑی کے مضبوط ہینگرز پر لٹکائیں۔", "Sardiyon ke wazni coat lakri ke mazboot hangers par latkayein."),
        ("The lining inside this suit jacket is made of smooth satin.", "کوٹ کے اندر کا استر نرم اور چمکدار ساٹن سے بنا ہے۔", "Coat ke andar ka astar narm aur chamakdaar satin se bana hai."),
        ("Dress respectfully according to the occasion and venue.", "ہمیشہ تقریب اور جگہ کے احترام کو مدنظر رکھ کر لباس پہنیں۔", "Hamesha taqreeb aur jagah ke ihtiram ko madd-e-nazar rakh kar libaas pehnein."),
        ("A bright smile and kind manners are the finest accessories.", "ایک روشن مسکراہٹ اور اچھا اخلاق ہی انسان کا بہترین زیور ہے۔", "Aik roshan muskurahat aur acha ikhlaaq hi insaan ka behtareen zewar hai."),
        ("Do you sell organic cotton shirts without synthetic polyester?", "کیا آپ کے پاس بغیر پولیسٹر کے خالص سوتی شرٹس ہیں؟", "Kya aap ke paas baghair polyester ke khalis sooti shirts hain?"),
        ("This trench coat protects from heavy wind and rain showers.", "یہ ٹرینچ کوٹ تیز ہوا اور بارش کی پھوار سے مکمل بچاتا ہے۔", "Yeh trench coat taiz hawa aur barish ki phuwaar se mukammal bachata hai."),
        ("Ensure your clothes are thoroughly dried before packing away.", "کپڑوں کو سنبھالنے سے پہلے یقینی بنائیں کہ وہ اچھی طرح خشک ہوں۔", "Kapron ko sambhalnay se pehle yaqeeni banayein ke woh achi tarah khushk hon."),
        ("Traditional Kashmiri embroidery is admired around the globe.", "روایتی کشمیری کڑھائی کو پوری دنیا میں قدر کی نگاہ سے دیکھا جاتا ہے۔", "Riwayati Kashmiri karhai ko poori dunya mein qadr ki nigah se dekha jata hai."),
        ("A clean white crisp dress shirt is a staple in every wardrobe.", "ایک صاف ستھری سفید قمیض ہر الماری کی بنیادی ضرورت ہوتی ہے۔", "Aik saaf suthri safaid qameez har almari ki bunyadi zaroorat hoti hai."),
        ("Dress with modesty, elegance, self-respect, and quiet confidence.", "حیا، وقار، خودداری اور اعتماد کے ساتھ لباس زیب تن کریں۔", "Haya, waqaar, khud-daari aur aitemad ke sath libaas zaib-e-tan karein."),
        ("These breathable socks prevent sweat and foot odor.", "یہ معیاری جرابیں پسینے اور پاؤں کی بو سے محفوظ رکھتی ہیں۔", "Yeh meyaari jurrabein paseenay aur paon ki boo se mahfooz rakhti hain."),
        ("He takes great care of his clothing and personal grooming.", "وہ اپنے لباس اور ذاتی بناؤ سنگھار کا بہت خیال رکھتا ہے۔", "Woh apne libaas aur zaati banav-singhar ka bohat khayal rakhta hai."),
        ("Confidence is what truly elevates any outfit you wear.", "آپ کا اعتماد ہی دراصل ہر لباس کو پرکشش اور باوقار بناتا ہے۔", "Aap ka aitemad hi dar-asal har libaas ko pur-kashish banata hai."),
        ("Wishing you a smart, stylish, and dignified appearance always!", "اللہ تعالیٰ آپ کے انداز کو ہمیشہ شاندار، باوقار اور پرکشش رکھے!", "Allah Ta'ala aap ke andaaz ko hamesha shandar aur ba-waqaar rakhay!")
    ]
    topics.append({
        "id": 27, "name": "Clothes & Fashion", "urduName": "لباس اور فیشن", "icon": "checkroom",
        "desc": "Dressing style, fabrics, colors, and traditional wear.", "sentences": t27
    })

    # Add 28, 29, 30
    import part3_topics_28_to_30
    part3_topics_28_to_30.add_28_to_30(topics)

print("Part 3 26-30 loaded.")
