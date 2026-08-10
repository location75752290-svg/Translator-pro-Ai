package com.example.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.local.AppDatabase
import com.example.data.local.TutorChatEntity
import com.example.data.remote.Content
import com.example.data.remote.GenerateContentRequest
import com.example.data.remote.GeminiClient
import com.example.data.remote.Part
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunsetAmber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

// Topics definition
enum class DailyTopic(
    val id: String,
    val title: String,
    val urduTitle: String,
    val icon: String,
    val description: String,
    val questions: List<Pair<String, String>>
) {
    ROUTINE(
        "routine",
        "Daily Routine",
        "روزانہ کا معمول",
        "⏰",
        "Practice talking about your daily routine step-by-step.",
        listOf(
            "Hello! Let's talk about your daily routine. What time do you wake up?" to "ہیلو! آئیے اپنے روزانہ کے معمول کے بارے میں بات کرتے ہیں۔ آپ کب اٹھتے ہیں؟",
            "Perfect! Now, let's move to breakfast. What do you usually eat for breakfast?" to "بہترین! اب، ناشتے کی طرف چلتے ہیں۔ آپ عام طور پر ناشتے میں کیا کھاتے ہیں؟",
            "Awesome! Tell me about your work or study routine. What do you do during the day?" to "بہت خوب! مجھے اپنے کام یا پڑھائی کے معمولات کے بارے میں بتائیں۔ آپ دن میں کیا کرتے ہیں؟",
            "That sounds busy and productive! How do you spend your evenings?" to "یہ تو بہت مصروف اور مفید معمول لگ رہا ہے! آپ اپنی شامیں کیسے گزارتے ہیں؟",
            "Nice! Finally, what time do you usually go to sleep?" to "اچھا! آخر میں، آپ عام طور پر کس وقت سوتے ہیں؟",
            "How do you manage your time on weekends compared to weekdays?" to "آپ ہفتے کے آخر میں کام کے دنوں کے مقابلے میں اپنے وقت کا انتظام کیسے کرتے ہیں؟",
            "What is your favorite part of your daily routine and why?" to "آپ کے روزمرہ کے معمولات کا سب سے پسندیدہ حصہ کون سا ہے اور کیوں؟",
            "Is there anything you would like to change or improve in your current daily schedule?" to "کیا آپ اپنے موجودہ روزمرہ کے شیڈول میں کچھ تبدیل یا بہتر کرنا چاہتے ہیں؟",
            "How do you handle stress or tiredness after a long busy day?" to "ایک طویل اور مصروف دن کے بعد آپ تناؤ یا تھکاوٹ کو کیسے سنبھالتے ہیں؟",
            "Do you prefer a morning routine or are you more productive during the night?" to "کیا آپ صبح کے معمولات کو ترجیح دیتے ہیں یا آپ رات کے وقت زیادہ فعال ہوتے ہیں؟",
            "Explain how maintaining a proper daily routine can help someone achieve long-term success." to "وضاحت کریں کہ کس طرح مناسب روزمرہ کا معمول کسی کو طویل مدتی کامیابی حاصل کرنے میں مدد دے سکتا ہے۔",
            "If you had 2 extra hours every day, what hobby or activity would you add to your schedule?" to "اگر آپ کے پاس روزانہ 2 اضافی گھنٹے ہوں، تو آپ اپنے شیڈول میں کون سا مشغلہ یا سرگرمی شامل کریں گے؟",
            "ROLEPLAY: I am your lazy friend who wakes up very late. Convince me to build a morning routine." to "رول پلے: میں آپ کا سست دوست ہوں جو بہت دیر سے اٹھتا ہے۔ مجھے صبح کا معمول بنانے پر قائل کریں۔",
            "How has your routine changed compared to 5 years ago?" to "5 سال پہلے کے مقابلے میں آپ کا معمول کتنا بدل گیا ہے؟",
            "What is your ultimate goal for a perfect, healthy daily routine?" to "ایک بہترین اور صحت مند روزمرہ کے معمول کے لیے آپ کا حتمی مقصد کیا ہے؟"
        )
    ),
    INTERVIEW(
        "interview",
        "Job Interview",
        "نوکری کا انٹرویو",
        "💼",
        "Prepare for job questions and professional English.",
        listOf(
            "Hello! Tell me about yourself." to "ہیلو! اپنا تعارف کروائیں؟",
            "What is your full name and qualification?" to "آپ کا پورا نام اور تعلیم کیا ہے؟",
            "Where are you from?" to "آپ کہاں سے ہیں؟",
            "Why do you want to do this job?" to "آپ یہ نوکری کیوں کرنا چاہتے ہیں؟",
            "Do you have any previous work experience?" to "کیا آپ کا کوئی پچھلا تجربہ ہے؟",
            "What are your 3 biggest strengths?" to "آپ کی 3 سب سے بڑی خوبیاں کیا ہیں؟",
            "What is your biggest weakness and how do you improve it?" to "آپ کی سب سے بڑی کمزوری کیا ہے؟",
            "Why should we hire you instead of others?" to "ہم آپ کو دوسروں کے بجائے کیوں رکھیں؟",
            "What are your salary expectations?" to "آپ کی تنخواہ کی توقع کیا ہے؟",
            "Are you comfortable working in team and under pressure?" to "کیا آپ ٹیم میں اور پریشر میں کام کر سکتے ہیں؟",
            "Tell me about a time you faced a challenge at work. How did you solve it?" to "کوئی مشکل بتائیں جو آپ نے حل کی؟",
            "Where do you see yourself after 5 years?" to "5 سال بعد آپ خود کو کہاں دیکھتے ہیں؟",
            "ROLEPLAY: I am an angry customer. Handle me politely in English." to "رول پلے: میں غصے والا کسٹمر ہوں",
            "Do you have any questions for me about this job?" to "کیا آپ کا مجھ سے کوئی سوال ہے؟",
            "Thank you for coming. We will contact you soon. How will you reply?" to "شکریہ۔ ہم آپ سے رابطہ کریں گے۔ جواب دیں؟"
        )
    ),
    SHOPPING(
        "shopping",
        "Shopping",
        "خریداری",
        "🛍️",
        "Practice retail terms, pricing, and buying conversation.",
        listOf(
            "Hello! Welcome to our clothing store. What are you looking to buy today?" to "ہیلو! ہماری کپڑوں کی دکان میں خوش آمدید۔ آج آپ کیا خریدنا چاہتے ہیں؟",
            "What color and size do you prefer for clothes?" to "آپ کپڑوں کے لیے کس رنگ اور سائز کو ترجیح دیتے ہیں؟",
            "Do you like to buy designer brands, or do you prefer simple local brands?" to "کیا آپ ڈیزائنر برانڈز خریدنا پسند کرتے ہیں، یا آپ سادہ مقامی برانڈز کو ترجیح دیتے ہیں؟",
            "How would you like to pay, cash or credit card?" to "آپ کس طرح ادائیگی کرنا پسند کرتے ہیں، نقد یا کریڈٹ کارڈ؟",
            "Do you want me to help you find matching shoes?" to "کیا آپ چاہتے ہیں کہ میں آپ کو ملتے جلتے جوتے تلاش کرنے میں مدد کروں؟",
            "What is the most expensive thing you have ever purchased?" to "آپ نے اب تک کی سب سے مہنگی چیز کون سی خریدی ہے؟",
            "Do you prefer shopping online or visiting physical stores?" to "کیا آپ آن لائن خریداری کو ترجیح دیتے ہیں یا جسمانی دکانوں پر جانا پسند کرتے ہیں؟",
            "How do you decide if a product is worth buying?" to "آپ کیسے فیصلہ کرتے ہیں کہ کوئی پروڈکٹ خریدنے کے قابل ہے یا نہیں؟",
            "Have you ever bought something that you never ended up using?" to "کیا آپ نے کبھی کوئی ایسی چیز خریدی ہے جسے آپ نے کبھی استعمال ہی نہ کیا ہو؟",
            "Do you like shopping during sales and festivals to get discounts?" to "کیا آپ ڈسکاؤنٹ حاصل کرنے کے لیے سیلز اور تہواروں کے دوران خریداری کرنا پسند کرتے ہیں؟",
            "How has e-commerce changed the traditional way people shop in your country?" to "ای کامرس نے آپ کے ملک میں روایتی خریداری کے طریقے کو کیسے بدل دیا ہے؟",
            "Do you think advertising tricks people into buying things they don't need?" to "کیا آپ کے خیال میں اشتہارات لوگوں کو ایسی چیزیں خریدنے پر مجبور کرتے ہیں جن کی انہیں ضرورت نہیں ہوتی؟",
            "ROLEPLAY: I am a shopkeeper refusing to refund a damaged item. Argue politely to get a refund." to "رول پلے: میں ایک دکاندار ہوں جو خراب شدہ چیز واپس کرنے سے انکار کر رہا ہے۔ واپسی کے لیے شائستگی سے بات کریں۔",
            "If you opened your own store, what kind of products would you sell?" to "اگر آپ اپنی دکان کھولیں تو آپ کس قسم کی مصنوعات فروخت کریں گے؟",
            "What is the best shopping advice you would give to save money?" to "پیسے بچانے کے لیے آپ خریداری کا کون سا بہترین مشورہ دیں گے؟"
        )
    ),
    DOCTOR(
        "doctor",
        "Doctor Visit",
        "ڈاکٹر کے پاس",
        "🩺",
        "Learn medical dialogue, symptoms, and advice terms.",
        listOf(
            "Hello! Please take a seat. What seems to be the problem today?" to "ہیلو! تشریف رکھیں۔ آج کیا مسئلہ لگ رہا ہے؟",
            "How long have you been suffering from these symptoms?" to "آپ کب سے ان علامات کا شکار ہیں؟",
            "Do you also have a fever or a headache?" to "کیا آپ کو بخار یا سر درد بھی ہے؟",
            "Are you currently taking any regular medicine?" to "کیا آپ فی الحال کوئی باقاعدہ دوا لے رہے ہیں؟",
            "Do you need a medical prescription?" to "کیا آپ کو طبی نسخہ چاہیے؟",
            "Do you have any known allergies to medicines or food?" to "کیا آپ کو ادویات یا کھانے سے کوئی الرجی ہے؟",
            "How often do you go for a general health checkup?" to "آپ عام طور پر صحت کے معائنے کے لیے کتنی بار جاتے ہیں؟",
            "What healthy habits do you practice to avoid getting sick?" to "بیمار ہونے سے بچنے کے لیے آپ کن صحت مند عادات پر عمل کرتے ہیں؟",
            "Do you prefer traditional medicine or home remedies for minor illnesses?" to "کیا آپ معمولی بیماریوں کے لیے روایتی ادویات یا گھریلو علاج کو ترجیح دیتے ہیں؟",
            "How do you maintain a balanced diet and stay active?" to "آپ متوازن غذا کیسے برقرار رکھتے ہیں اور فعال رہتے ہیں؟",
            "What are the biggest challenges facing the healthcare system today?" to "آج کے ہیلتھ کیئر سسٹم کو درپیش سب سے بڑے چیلنجز کیا ہیں؟",
            "How has technology or AI improved medical treatments in recent years?" to "حالیہ برسوں میں ٹیکنالوجی یا اے آئی نے طبی علاج کو کیسے بہتر بنایا ہے؟",
            "ROLEPLAY: I am a patient who is afraid of getting an injection. Calm me down and convince me." to "رول پلے: میں ایک مریض ہوں جو انجیکشن لگوانے سے ڈرتا ہے۔ مجھے پرسکون کریں اور قائل کریں۔",
            "What advice would you give to teenagers to live a healthier life?" to "آپ نوعمروں کو صحت مند زندگی گزارنے کے لیے کیا مشورہ دیں گے؟",
            "Why is mental health as important as physical health?" to "دہنی صحت جسمانی صحت کی طرح کیوں اہم ہے؟"
        )
    ),
    TRAVEL(
        "travel",
        "Travel & Airport",
        "سفر اور ہوائی اڈا",
        "✈️",
        "Master travel, airport queries, and location talk.",
        listOf(
            "Hello! Where is your favorite travel destination?" to "ہیلو! آپ کی پسندیدہ ترین سفری جگہ کون سی ہے؟",
            "Do you prefer windows seats or aisle seats on flights?" to "کیا آپ پروازوں میں کھڑکی والی نشستوں کو ترجیح دیتے ہیں یا گلیارے والی نشستوں کو؟",
            "How many bags are you planning to check in today?" to "آپ آج کتنے بیگ چیک ان کرنے کا ارادہ رکھتے ہیں؟",
            "Have you got your boarding pass and passport ready?" to "کیا آپ نے اپنا بورڈنگ پاس اور پاسپورٹ تیار کر لیا ہے؟",
            "What is your favorite vacation activity?" to "آپ کی چھٹیوں کی پسندیدہ سرگرمی کیا ہے؟",
            "Do you prefer traveling alone or with a group of friends or family?" to "کیا آپ اکیلے سفر کرنا پسند کرتے ہیں یا دوستوں یا خاندان کے گروپ کے ساتھ؟",
            "What was your most memorable travel experience so far?" to "اب تک کا آپ کا سب سے یادگار سفری تجربہ کون سا تھا؟",
            "How do you usually plan your travel budget?" to "آپ عام طور پر اپنے سفر کے بجٹ کی منصوبہ بندی کیسے کرتے ہیں؟",
            "Would you rather visit a historical city or a relaxing beach resort?" to "کیا آپ تاریخی شہر کی سیر کرنا پسند کریں گے یا کسی پرسکون ساحلی ریزورٹ کی؟",
            "What are the essential items you always pack in your suitcase?" to "وہ کون سی ضروری چیزیں ہیں جو آپ ہمیشہ اپنے سوٹ کیس میں پیک کرتے ہیں؟",
            "How does traveling to a new country change a person's perspective on life?" to "کسی نئے ملک کا سفر کرنے سے انسان کا زندگی کے بارے میں نظریہ کیسے بدلتا ہے؟",
            "What is the biggest challenge of traveling to a place where you don't know the language?" to "کسی ایسی جگہ کا سفر کرنے کا سب سے بڑا چیلنج کیا ہے جہاں کی زبان آپ نہیں جانتے؟",
            "ROLEPLAY: I am an immigration officer asking for your hotel booking. Answer confidently." to "رول پلے: میں ایک امیگریشن آفیسر ہوں جو آپ کے ہوٹل کی بکنگ مانگ رہا ہو۔ اعتماد کے ساتھ جواب دیں۔",
            "If you had unlimited money, where in the world would you travel first?" to "اگر آپ کے پاس لامحدود پیسہ ہو، تو آپ دنیا میں سب سے پہلے کہاں کا سفر کریں گے؟",
            "Why is tourism important for the economy of a country?" to "سیاحت کسی ملک کی معیشت کے لیے کیوں اہم ہے؟"
        )
    ),
    RESTAURANT(
        "restaurant",
        "Restaurant",
        "ریسٹورنٹ میں",
        "🍔",
        "Order food, talk about preferences and recipes.",
        listOf(
            "Hello! Welcome to Sarah's Kitchen. What would you like to order today?" to "ہیلو! سارہ کے کچن میں خوش آمدید۔ آج آپ کیا آرڈر کرنا چاہیں گے؟",
            "Would you like some soup or salad as an appetizer?" to "کیا آپ بھوک بڑھانے کے لیے کچھ سوپ یا سلاد پسند کریں گے؟",
            "Do you want your food to be extra spicy or mild?" to "کیا آپ چاہتے ہیں کہ آپ کا کھانا زیادہ مسالے دار ہو یا ہلکا؟",
            "What would you like to have for drinks?" to "آپ مشروبات میں کیا لینا پسند کریں گے؟",
            "Would you like to try our special dessert of the day?" to "کیا آپ ہمارا آج کا خصوصی میٹھا آزمانا چاہیں گے؟",
            "What is your favorite cuisine (e.g., Pakistani, Chinese, Italian, Fast Food)?" to "آپ کا پسندیدہ کھانا کون سا ہے (مثال کے طور پر، پاکستانی، چینی، اطالوی، فاسٹ فوڈ)؟",
            "Do you prefer eating out at restaurants or eating home-cooked meals?" to "کیا آپ ریستوراں میں کھانا کھانے کو ترجیح دیتے ہیں یا گھر کا پکا ہوا کھانا پسند کرتے ہیں؟",
            "How do you rate the service quality of a restaurant?" to "آپ کسی ریستوراں کی سروس کے معیار کی درجہ بندی کیسے کرتے ہیں؟",
            "Can you cook? What is the best dish you can prepare?" to "کیا آپ کھانا پکا سکتے ہیں؟ آپ کون سی بہترین ڈش تیار کر سکتے ہیں؟",
            "Have you ever had a bad experience at a restaurant? What happened?" to "کیا آپ کا کبھی کسی ریستوراں میں برا تجربہ ہوا ہے؟ کیا ہوا تھا؟",
            "Do you think fast food is a major cause of health problems nowadays?" to "کیا آپ کے خیال میں آج کل فاسٹ فوڈ صحت کے مسائل کی ایک بڑی وجہ ہے؟",
            "If you were to open a restaurant, what theme and menu would you choose?" to "اگر آپ کو ایک ریستوراں کھولنا ہو، تو آپ کون سا تھیم اور مینو منتخب کریں گے؟",
            "ROLEPLAY: I am a waiter who brought you the wrong order. Explain the issue politely." to "رول پلے: میں ایک ویٹر ہوں جو آپ کا غلط آرڈر لے آیا ہے۔ شائستگی سے مسئلہ واضح کریں۔",
            "Why has food delivery services become so popular recently?" to "حالیہ دنوں میں فوڈ ڈیلیوری سروسز اتنی مقبول کیوں ہو گئی ہیں؟",
            "What role does food play in the culture and hospitality of your country?" to "آپ کے ملک کی ثقافت اور مہمان نوازی میں کھانا کیا کردار ادا کرتا ہے؟"
        )
    ),
    FRIENDS_FAMILY(
        "friends_family",
        "Friends & Family",
        "دوست اور فیملی",
        "👨‍👩‍👧‍👦",
        "Describe relationships, relatives, and close friends.",
        listOf(
            "Hello! Tell me about your best friend. What is their name?" to "ہیلو! مجھے اپنے بہترین دوست کے بارے میں بتائیں۔ ان کا نام کیا ہے؟",
            "How do you and your family usually celebrate birthdays?" to "آپ اور آپ کا خاندان عام طور پر سالگرہ کیسے مناتے ہیں؟",
            "Do you live in a big house or a small cozy apartment?" to "کیا آپ ایک بڑے گھر میں رہتے ہیں یا ایک چھوٹے آرام دہ اپارٹمنٹ میں؟",
            "What is your favorite family holiday destination?" to "آپ کے خاندان کی پسندیدہ چھٹیوں کی جگہ کون سی ہے؟",
            "Who is the funniest person in your family?" to "آپ کے خاندان میں سب سے زیادہ مزاحیہ شخص کون ہے؟",
            "What qualities do you value most in a good friend?" to "آپ ایک اچھے دوست میں کن خصوصیات کو سب سے زیادہ اہمیت دیتے ہیں؟",
            "How do you spend quality time with your family on weekends?" to "آپ ہفتے کے آخر میں اپنے خاندان کے ساتھ معیاری وقت کیسے گزارتے ہیں؟",
            "Do you share your secrets with your friends or keep them to yourself?" to "کیا آپ اپنے راز اپنے دوستوں کے ساتھ شیئر کرتے ہیں یا اپنے پاس رکھتے ہیں؟",
            "How does your family support you in your goals and education?" to "آپ کا خاندان آپ کے مقاصد اور تعلیم میں آپ کی کیسے مدد کرتا ہے؟",
            "What is a special tradition or custom in your family?" to "آپ کے خاندان میں کوئی خاص روایت یا رواج کیا ہے؟",
            "How do you balance your time between your friends and your family?" to "آپ اپنے دوستوں اور اپنے خاندان کے درمیان اپنے وقت کو کیسے متوازن کرتے ہیں؟",
            "Do you think social media has made people closer or more distant from family?" to "کیا آپ کے خیال میں سوشل میڈیا نے لوگوں کو خاندان کے قریب کیا ہے یا دور؟",
            "ROLEPLAY: I am your cousin asking to borrow a large sum of money. Decline or accept politely." to "رول پلے: میں آپ کا کزن ہوں جو بڑی رقم ادھار مانگ رہا ہے۔ شائستگی سے انکار یا قبول کریں۔",
            "How have your friendships changed as you have grown older?" to "آپ کے بڑے ہونے کے ساتھ آپ کی دوستی میں کیا تبدیلیاں آئی ہیں؟",
            "What is the key to maintaining a strong, long-lasting relationship?" to "ایک مضبوط اور دیرپا رشتہ برقرار رکھنے کی کلید کیا ہے؟"
        )
    ),
    STUDY(
        "study",
        "School & Study",
        "اسکول اور پڑھائی",
        "📚",
        "Discuss books, schools, and academic progress.",
        listOf(
            "Hello! What is your favorite subject at school or college?" to "ہیلو! اسکول یا کالج میں آپ کا پسندیدہ مضمون کون سا ہے؟",
            "How many hours do you spend studying every day?" to "آپ ہر روز پڑھائی پر کتنے گھنٹے گزارتے ہیں؟",
            "Do you prefer reading physical books or e-books on a tablet?" to "کیا آپ فزیکل کتابیں پڑھنا پسند کرتے ہیں یا ٹیبلٹ پر ای بکس؟",
            "What is your favorite memory from your school days?" to "آپ کے اسکول کے دنوں کی سب سے پسندیدہ یاد کون سی ہے؟",
            "Do you want to study abroad in the future?" to "کیا آپ مستقبل میں بیرون ملک پڑھنا چاہتے ہیں؟",
            "How do you prepare for difficult exams or tests?" to "آپ مشکل امتحانات یا ٹیسٹوں کی تیاری کیسے کرتے ہیں؟",
            "Who is your favorite teacher and why do you like them?" to "آپ کا پسندیدہ استاد کون ہے اور آپ انہیں کیوں پسند کرتے ہیں؟",
            "Do you prefer group study with classmates or studying alone in silence?" to "کیا آپ ہم جماعتوں کے ساتھ گروپ اسٹڈی کو ترجیح دیتے ہیں یا اکیلے خاموشی سے پڑھنا؟",
            "What is the most challenging subject for you, and how do you study it?" to "آپ کے لیے سب سے مشکل مضمون کون سا ہے، اور آپ اسے کیسے پڑھتے ہیں؟",
            "How do you avoid distractions like social media while studying?" to "پڑھائی کے دوران آپ سوشل میڈیا جیسی خلفشار سے کیسے بچتے ہیں؟",
            "What are the benefits of online learning compared to traditional classrooms?" to "روایتی کلاس رومز کے مقابلے آن لائن سیکھنے کے کیا فائدے ہیں؟",
            "How do you think the modern education system can be improved?" to "آپ کے خیال میں جدید نظام تعلیم کو کیسے بہتر بنایا جا سکتا ہے؟",
            "ROLEPLAY: I am your teacher asking why you didn't submit your assignment. Explain politely." to "رول پلے: میں آپ کا ٹیچر ہوں جو پوچھ رہا ہے کہ آپ نے اسائنمنٹ کیوں جمع نہیں کرائی۔ شائستگی سے وضاحت کریں۔",
            "Why is lifelong learning important even after completing formal education?" to "رسمی تعلیم مکمل کرنے کے بعد بھی تاحیات سیکھنا کیوں ضروری ہے؟",
            "What is your dream career, and how is your education helping you reach it?" to "آپ کا خوابیدہ کیریئر کیا ہے، اور آپ کی تعلیم اسے حاصل کرنے میں آپ کی کیسے مدد کر رہی ہے؟"
        )
    ),
    WEATHER(
        "weather",
        "Weather & News",
        "موسم اور خبریں",
        "🌤️",
        "Discuss climate changes, seasons, and latest news.",
        listOf(
            "Hello! How is the weather in your area today?" to "ہیلو! آج آپ کے علاقے میں موسم کیسا ہے؟",
            "Do you like rainy days or do you prefer sunny weather?" to "کیا آپ کو بارش کے دن پسند ہیں یا آپ کو دھوپ والا موسم پسند ہے؟",
            "How do you stay updated with the daily news?" to "آپ روزانہ کی خبروں سے کیسے باخبر رہتے ہیں؟",
            "What was the most interesting news topic you read this week?" to "اس ہفتے آپ نے کون سا سب سے دلچسپ خبر کا موضوع پڑھا؟",
            "Do you think weather forecasts are always accurate?" to "کیا آپ کے خیال میں weather forecast ہمیشہ درست ہوتی ہے؟",
            "What is your favorite season of the year and why?" to "سال کا آپ کا سب سے پسندیدہ موسم کون سا ہے اور کیوں؟",
            "How does extreme hot or cold weather affect your daily routine?" to "انتہائی گرم یا سرد موسم آپ کے روزمرہ کے معمولات کو کیسے متاثر کرتا ہے؟",
            "Do you prefer reading local news, or are you more interested in global news?" to "کیا آپ مقامی خبریں پڑھنے کو ترجیح دیتے ہیں، یا آپ عالمی خبروں میں زیادہ دلچسپی رکھتے ہیں؟",
            "What is your favorite indoor activity when the weather is bad outside?" to "جب باہر موسم خراب ہو تو آپ کی پسندیدہ انڈور سرگرمی کون سی ہے؟",
            "How do people in your city prepare for heavy monsoon rains?" to "آپ کے شہر میں لوگ مون سون کی شدید بارشوں کی تیاری کیسے کرتے ہیں؟",
            "What are the visible effects of climate change that you have noticed in your country?" to "آپ نے اپنے ملک میں موسمیاتی تبدیلی کے کون سے نمایاں اثرات محسوس کیے ہیں؟",
            "Do you think modern journalism focuses too much on negative news rather than positive news?" to "کیا آپ کے خیال میں جدید صحافت مثبت خبروں کے بجائے منفی خبروں پر زیادہ توجہ دیتی ہے؟",
            "ROLEPLAY: I am a reporter asking you for an eye-witness report of a heavy storm in your city." to "رول پلے: میں ایک رپورٹر ہوں جو آپ سے آپ کے شہر میں شدید طوفان کی عینی شاہد رپورٹ مانگ رہا ہوں۔",
            "How can individuals help in reducing air pollution and protecting the environment?" to "افراد فضائی آلودگی کو کم کرنے اور ماحول کے تحفظ میں کیسے مدد کر سکتے ہیں؟",
            "Why is freedom of press important for a developing society?" to "ترقی پذیر معاشرے کے لیے پریس کی آزادی کیوں اہم ہے؟"
        )
    ),
    SMALL_TALK(
        "small_talk",
        "Compliments & Small Talk",
        "تعریف اور گپ شپ",
        "💬",
        "Aesthetic compliments, weather breakers, and warm chats.",
        listOf(
            "Hello! Your smile is so bright and positive. How is your week going?" to "ہیلو! آپ کی مسکراہٹ بہت روشن اور مثبت ہے۔ آپ کا ہفتہ کیسا گزر رہا ہے؟",
            "What is your favorite way to spend a relaxing Sunday?" to "اتوار کا پرسکون دن گزارنے کا آپ کا پسندیدہ طریقہ کیا ہے؟",
            "If you could have any superpower, what would it be?" to "اگر آپ کے پاس کوئی سپر پاور ہو سکتی ہے تو وہ کیا ہوگی؟",
            "What are some of the things that always make you happy?" to "کچھ ایسی چیزیں کون سی ہیں جو آپ کو ہمیشہ خوش کرتی ہیں؟",
            "What are you most grateful for today?" to "آج آپ کس چیز کے لیے سب سے زیادہ شکر گزار ہیں؟",
            "Do you believe in luck or do you think hard work determines success?" to "کیا آپ قسمت پر یقین رکھتے ہیں یا آپ کے خیال میں سخت محنت کامیابی کا تعین کرتی ہے؟",
            "If you could meet any famous person, alive or dead, who would it be?" to "اگر آپ کسی بھی مشہور شخص سے مل سکتے ہیں، زندہ یا مردہ، تو وہ کون ہوگا؟",
            "What is your absolute favorite book or movie and why?" to "آپ کی مطلق پسندیدہ کتاب یا فلم کون سی ہے اور کیوں؟",
            "What are some of your long-term dreams or goals?" to "آپ کے کچھ طویل مدتی خواب یا مقاصد کیا ہیں؟",
            "How do you stay motivated when things get difficult?" to "جب حالات مشکل ہو جاتے ہیں تو آپ متحرک کیسے رہتے ہیں؟",
            "How do you think technology has changed the way we form friendships today?" to "آپ کے خیال میں ٹیکنالوجی نے ہمارے دوست بنانے کے طریقے کو کیسے بدل دیا ہے؟",
            "If you could travel back in time, which era would you visit and why?" to "اگر آپ وقت میں پیچھے سفر کر سکتے ہیں، تو آپ کس دور کا دورہ کریں گے اور کیوں؟",
            "ROLEPLAY: I am a shy stranger sitting next to you on a bus. Start a polite conversation with me." to "رول پلے: میں ایک شرمیلا اجنبی ہوں جو بس میں آپ کے ساتھ بیٹھا ہے۔ میرے ساتھ ایک شائستہ گفتگو شروع کریں۔",
            "What is the best piece of life advice you have ever received?" to "آپ کو زندگی کا اب تک کا سب سے بہترین مشورہ کون سا ملا ہے؟",
            "What does success mean to you personally?" to "شخصی طور پر آپ کے لیے کامیابی کا کیا مطلب ہے؟"
        )
    );

    val initialEn: String get() = questions[0].first
    val initialUr: String get() = questions[0].second

    companion object {
        fun fromId(id: String): DailyTopic {
            return values().firstOrNull { it.id == id } ?: ROUTINE
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiConversationPartnerScreen(
    onBackClick: () -> Unit,
    onNavigateToGrammar: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Database Setup
    val db = remember { AppDatabase.getDatabase(context) }
    val tutorChatDao = remember { db.tutorChatDao() }

    // Selected Topic
    var activeTopic by remember { mutableStateOf(DailyTopic.ROUTINE) }
    var showTopicSelection by remember { mutableStateOf(true) }

    // Progress and XP tracking variables
    val topicPrefs = remember { context.getSharedPreferences("topic_partner_prefs", Context.MODE_PRIVATE) }
    var showCompletionDialog by remember { mutableStateOf(false) }
    var showFeedbackReportDialog by remember { mutableStateOf(false) }
    var hasTriggeredCompletionInSession by remember { mutableStateOf(false) }
    var userXp by remember { mutableStateOf(topicPrefs.getInt("user_xp", 100)) }

    LaunchedEffect(showTopicSelection, showCompletionDialog) {
        userXp = topicPrefs.getInt("user_xp", 100)
    }

    LaunchedEffect(activeTopic) {
        hasTriggeredCompletionInSession = false
    }

    val nextTopic = remember(activeTopic) {
        val values = DailyTopic.values()
        val currentIndex = values.indexOf(activeTopic)
        if (currentIndex != -1 && currentIndex < values.size - 1) {
            values[currentIndex + 1]
        } else {
            values[0] // Loop back to the first
        }
    }

    // Screen State variables
    var isGeminiMode by remember { mutableStateOf(false) }
    var isLoadingGemini by remember { mutableStateOf(false) }
    var userSpokenText by remember { mutableStateOf("") }

    // Load messages from database dynamically
    val chatMessagesFlow = remember(activeTopic) {
        tutorChatDao.getChatMessages("partner_" + activeTopic.id)
    }
    val chatMessages by chatMessagesFlow.collectAsState(initial = emptyList())

    val pastTenseMistakeCount = remember(chatMessages) {
        var count = 0
        val presentVerbs = listOf("go", "eat", "wake", "sleep", "work", "play", "study", "run", "buy", "see", "take", "make", "write", "speak", "tell", "teach")
        val pastIndicators = listOf("yesterday", "ago", "last", "past", "childhood", "school", "then", "young", "achieved", "was", "were", "did", "earlier", "achieve", "success", "memory")
        
        chatMessages.forEach { msg ->
            if (msg.sender == "user") {
                val text = msg.messageText.lowercase()
                val correction = msg.grammarCorrection?.lowercase() ?: ""
                
                val corrMentionsPast = correction.contains("past") || correction.contains("tense") || 
                        correction.contains("should be past") || correction.contains("use past") ||
                        (correction.contains("instead of") && (correction.contains("went") || correction.contains("was") || correction.contains("had") || correction.contains("did") || correction.contains("worked") || correction.contains("played") || correction.contains("studied") || correction.contains("slept")))
                
                val userUsedPresentWithPastIndicator = msg.grammarCorrection != null && 
                        pastIndicators.any { text.contains(it) } && 
                        presentVerbs.any { text.contains(it) }
                
                val manualPastTenseError = text.contains("yesterday") && (text.contains("go") || text.contains("eat") || text.contains("sleep") || text.contains("work") || text.contains("play") || text.contains("study") || text.contains("run") || text.contains("buy") || text.contains("see"))
                
                if (corrMentionsPast || userUsedPresentWithPastIndicator || manualPastTenseError) {
                    count++
                }
            }
        }
        count
    }

    LaunchedEffect(chatMessages, activeTopic, showTopicSelection) {
        if (!showTopicSelection) {
            val userMsgCount = chatMessages.count { it.sender == "user" }
            if (userMsgCount >= activeTopic.questions.size && !hasTriggeredCompletionInSession) {
                hasTriggeredCompletionInSession = true
                val todayDate = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())
                val todayKey = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
                val yesterdayCal = java.util.Calendar.getInstance()
                yesterdayCal.add(java.util.Calendar.DAY_OF_YEAR, -1)
                val yesterdayKey = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(yesterdayCal.time)

                val currentStreak = topicPrefs.getInt("topic_streak_count", 0)
                val storedLastDate = topicPrefs.getString("topic_last_streak_date", "")

                val newStreak = if (storedLastDate == todayKey) {
                    currentStreak
                } else if (storedLastDate == yesterdayKey) {
                    currentStreak + 1
                } else {
                    1
                }

                topicPrefs.edit()
                    .putBoolean("topic_completed_${activeTopic.id}", true)
                    .putString("topic_date_${activeTopic.id}", todayDate)
                    .putInt("user_xp", topicPrefs.getInt("user_xp", 100) + 100)
                    .putBoolean("daily_goal_topic_completed_$todayKey", true)
                    .putString("last_completed_topic_date", todayKey)
                    .putString("topic_last_streak_date", todayKey)
                    .putInt("topic_streak_count", newStreak)
                    .apply()
                
                // Slight delay to allow final message to be spoken/shown
                kotlinx.coroutines.delay(1500)
                showCompletionDialog = true
            }
        }
    }

    // Lazy List State for chat scrolling
    val lazyListState = rememberLazyListState()

    // TTS Setup
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val speech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                isTtsReady = true
            }
        }
        tts = speech
        onDispose {
            speech.stop()
            speech.shutdown()
        }
    }

    fun speak(text: String) {
        if (isTtsReady && tts != null) {
            val cleanText = text
                .substringBefore("/") // Only read English part of translation if formatted together
                .replace("You said:", "")
                .replace("Correct:", "Correct is:")
                .trim()
            tts?.language = Locale.US
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "partner_speak")
        }
    }

    // Auto check if Gemini API key exists
    LaunchedEffect(Unit) {
        val key = GeminiClient.getApiKey()
        isGeminiMode = key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    // Insert first AI message if database list is empty for the current topic
    var lastSpokenTopic by remember { mutableStateOf<DailyTopic?>(null) }
    LaunchedEffect(activeTopic, chatMessages, showTopicSelection) {
        if (!showTopicSelection) {
            if (chatMessages.isEmpty()) {
                withContext(Dispatchers.IO) {
                    tutorChatDao.insertMessage(
                        TutorChatEntity(
                            scenarioId = "partner_" + activeTopic.id,
                            sender = "ai",
                            messageText = activeTopic.initialEn + " / " + activeTopic.initialUr,
                            grammarCorrection = null,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
            if (lastSpokenTopic != activeTopic && chatMessages.isNotEmpty()) {
                lastSpokenTopic = activeTopic
                val firstMsg = chatMessages.firstOrNull { it.sender == "ai" }?.messageText ?: activeTopic.initialEn
                speak(firstMsg)
            }
        }
    }

    // Auto scroll to bottom when a new message is added
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            lazyListState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    // Permission Launcher
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
    }

    // Offline state machine
    fun getOfflineResponse(topic: DailyTopic, input: String): Pair<String, String?> {
        val lower = input.lowercase()
        val stepIndex = chatMessages.count { it.sender == "user" }
        
        // Common corrections
        val hasUrdu = lower.contains("main") || lower.contains("hoon") || lower.contains("hun") || 
                     lower.contains("baje") || lower.contains("kar") || lower.contains("gaya") || 
                     lower.contains("hai") || lower.contains("haan") || lower.contains("na") ||
                     lower.contains("kiya") || lower.contains("khaya")
                     
        var correction: String? = null
        if (hasUrdu) {
            correction = "Great try! Always try to explain in English so we can practice. Very good!"
        }

        // Generic past tense check for offline responses
        val presentVerbs = listOf("go", "eat", "wake", "sleep", "work", "play", "study", "run", "buy", "see", "take", "make", "write", "speak", "tell", "teach")
        val hasPastIndicator = lower.contains("yesterday") || lower.contains("ago") || lower.contains("last") || lower.contains("childhood") || lower.contains("memory")
        if (hasPastIndicator && presentVerbs.any { lower.contains(it) }) {
            val usedVerb = presentVerbs.first { lower.contains(it) }
            val correctedVerb = when (usedVerb) {
                "go" -> "went"
                "eat" -> "ate"
                "wake" -> "woke"
                "sleep" -> "slept"
                "work" -> "worked"
                "play" -> "played"
                "study" -> "studied"
                "run" -> "ran"
                "buy" -> "bought"
                "see" -> "saw"
                "take" -> "took"
                "make" -> "made"
                "write" -> "wrote"
                "speak" -> "spoke"
                "tell" -> "told"
                "teach" -> "taught"
                else -> "did"
            }
            correction = "Grammar error: Use past tense form '$correctedVerb' instead of '$usedVerb' when talking about the past."
        }

        // Specific routine corrections
        if (topic == DailyTopic.ROUTINE) {
            if (stepIndex == 0 && (lower.contains("wake up 7") || lower.contains("wake 7") || (lower.contains("wake") && !lower.contains("at") && lower.contains("7")))) {
                correction = "Not 'I wake up 7 oclock'. Say 'I wake up AT 7 oclock'"
            } else if (stepIndex == 1 && (lower.contains("eat breakfast egg") || lower.contains("eat egg breakfast") || lower.contains("breakfast egg"))) {
                correction = "Not 'I eat breakfast egg'. Say 'I eat eggs FOR breakfast'"
            } else if (stepIndex == 2 && (lower.contains("working in office") || (lower.contains("working") && !lower.contains("am")))) {
                correction = "Not 'I working in office'. Say 'I work in an office'"
            } else if (stepIndex == 3 && (lower.contains("go walk") || lower.contains("walk in evening"))) {
                correction = "Not 'I go walk in evening'. Say 'I go FOR A walk in THE evening'"
            } else if (stepIndex == 4 && (lower.contains("sleep 11") || lower.contains("sleep 10") || lower.contains("sleep 12") || lower.contains("go sleep"))) {
                correction = "Not 'I sleep 11'. Say 'I go to sleep AT 11 o'clock'"
            }
        }

        if (stepIndex < topic.questions.size - 1) {
            val nextQ = topic.questions[stepIndex + 1]
            return Pair("${nextQ.first} / ${nextQ.second}", correction)
        } else {
            val endMessage = if (topic.id == "interview") {
                "🎉 Congratulations! You completed Job Interview Topic. +100 XP / مبارک ہو! آپ نے نوکری کا انٹرویو مکمل کر لیا ہے۔ +100 ایکس پی"
            } else {
                "Excellent practice! You did great today. Want to practice again tomorrow? / بہترین مشق! آپ نے آج بہت اچھا کام کیا۔ کیا آپ کل دوبارہ مشق کرنا چاہتے ہیں؟"
            }
            return Pair(endMessage, correction)
        }
    }

    // Function to generate STT pronunciation feedback
    fun getPronunciationAnalysis(input: String): String {
        val lower = input.lowercase(java.util.Locale.getDefault())
        val rawWords = lower.split("\\s+".toRegex()).map { it.replace(Regex("[^a-zA-Z]"), "") }.filter { it.isNotBlank() }

        val phoneticsMap = mapOf(
            "strength" to "Streng-th",
            "comfortable" to "Comf-ta-ble",
            "schedule" to "Skeh-jool",
            "vocabulary" to "Vo-cab-u-la-ry",
            "practice" to "Prak-tis",
            "interview" to "In-ter-vyoo",
            "experience" to "Ek-speer-ee-uhns",
            "opportunity" to "Op-per-too-ni-tee",
            "challenge" to "Chal-inj",
            "favorite" to "Fay-vrut",
            "breakfast" to "Brek-fust",
            "development" to "Dih-vel-up-munt",
            "manager" to "Man-i-jer",
            "college" to "Kol-ij",
            "working" to "Wur-king",
            "routine" to "Roo-teen",
            "morning" to "Mor-ning",
            "english" to "Ing-glish",
            "conversation" to "Kon-ver-say-shun"
        )

        var targetWord = "strength"
        var phonetic = "Streng-th"

        val matchedWord = rawWords.firstOrNull { phoneticsMap.containsKey(it) }
        if (matchedWord != null) {
            targetWord = matchedWord
            phonetic = phoneticsMap[matchedWord]!!
        } else {
            val longestWord = rawWords.maxByOrNull { it.length }
            if (longestWord != null && longestWord.length >= 4) {
                targetWord = longestWord
                phonetic = longestWord.take(3).replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() } + "-" + longestWord.drop(3)
            }
        }

        val capitalizedWord = targetWord.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
        val score = (8 + (input.length % 3)).coerceIn(8, 10)

        return "Good answer! Pronunciation Score: $score/10.\nTip: '$capitalizedWord' is pronounced '$phonetic'"
    }

    // Call Gemini API dynamically
    fun evaluateWithGemini(input: String) {
        isLoadingGemini = true
        coroutineScope.launch(Dispatchers.IO) {
            val apiKey = GeminiClient.getApiKey()
            
            // Build history context for prompt
            val historyBuilder = StringBuilder()
            chatMessages.takeLast(6).forEach { msg ->
                historyBuilder.append("${if (msg.sender == "user") "User" else "Sarah (Teacher)"}: ${msg.messageText}\n")
            }

            val prompt = """
                You are ${if (activeTopic.id == "interview") "the HR Manager" else "Sarah, a friendly English teacher"} helping a native Urdu speaker practice conversational English.
                We are currently practicing the topic: "${activeTopic.title}" (${activeTopic.description}).
                
                ### CONVERSATION FLOW ###
                This topic has exactly ${activeTopic.questions.size} questions:
                ${activeTopic.questions.mapIndexed { idx, q -> "Question ${idx + 1}: ${q.first}" }.joinToString("\n")}
                
                ### CONVERSATION HISTORY ###
                ${historyBuilder.toString()}
                
                ### USER'S LATEST RESPONSE ###
                "$input"
                
                ### YOUR JOB ###
                1. If the user responds in Urdu or Roman-Urdu, translate it to English, praise them, and encourage them on how to say it.
                   Example:
                   - User says: "Main 7 baje uthta hun" or "Main 7 baje uthta hoon"
                   - You correction response must be: "Great! In English: I wake up at 7 o'clock. Very good!"
                2. Correct English grammatical mistakes gently.
                   Example:
                   - User says: "I wake up 7 oclock"
                   - You correction response must be: "Not 'I wake up 7 oclock'. Say 'I wake up AT 7 oclock'"
                3. Ask the next question about "${activeTopic.title}" one by one. Ask only ONE question at a time.
                4. Based on the conversation history, identify which question was just answered, and ask the NEXT question from the sequence above.
                5. If the user has just answered the final Question ${activeTopic.questions.size} ("${activeTopic.questions.last().first}"), you MUST end the conversation with exactly:
                   "${if (activeTopic.id == "interview") "🎉 Congratulations! You completed Job Interview Topic. +100 XP" else "Excellent practice! You did great today. Want to practice again tomorrow?"}"
                6. Translate your reply and follow-up question into Urdu.
                7. AI Personality and Style:
                   ${if (activeTopic.id == "interview") {
                       "- Be professional but friendly. Act like an HR Manager.\n- Use encouraging feedback like: 'Good!', 'Excellent!', 'Very professional answer' to boost their confidence."
                   } else {
                       "- Be Sarah, very supportive, friendly, and helpful."
                   }}
                
                You MUST respond strictly with a valid JSON object in this format (no markdown formatting, no backticks, no wrapping code blocks):
                {
                  "correction": "Correction text if they made a mistake or used Urdu. Else leave empty or null.",
                  "reply_en": "Your conversational reply and next question in English",
                  "reply_ur": "Urdu translation of your reply and question"
                }
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )

            try {
                val response = GeminiClient.service.generateContent(apiKey, request)
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                
                val cleanedJson = rawText.trim()
                    .removePrefix("```json")
                    .removePrefix("```")
                    .removeSuffix("```")
                    .trim()

                val jsonObject = JSONObject(cleanedJson)
                val correction = jsonObject.optString("correction", "").ifBlank { null }
                val rawReplyEn = jsonObject.optString("reply_en", "That's great! Let's continue speaking.")
                val replyUr = jsonObject.optString("reply_ur", "یہ تو بہت اچھا ہے! آئیے بات جاری رکھیں۔")

                val pronAnalysis = getPronunciationAnalysis(input)
                val replyEn = if (!rawReplyEn.startsWith("Good answer! Pronunciation Score:")) {
                    "$pronAnalysis\n\n$rawReplyEn"
                } else {
                    rawReplyEn
                }

                withContext(Dispatchers.Main) {
                    // Save User's Message
                    tutorChatDao.insertMessage(
                        TutorChatEntity(
                            scenarioId = "partner_" + activeTopic.id,
                            sender = "user",
                            messageText = input,
                            grammarCorrection = correction,
                            timestamp = System.currentTimeMillis()
                        )
                    )

                    // Save AI's Message
                    val aiText = "$replyEn / $replyUr"
                    tutorChatDao.insertMessage(
                        TutorChatEntity(
                            scenarioId = "partner_" + activeTopic.id,
                            sender = "ai",
                            messageText = aiText,
                            grammarCorrection = null,
                            timestamp = System.currentTimeMillis()
                        )
                    )

                    speak(replyEn)
                    isLoadingGemini = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback to offline on error
                withContext(Dispatchers.Main) {
                    val (rawReply, correction) = getOfflineResponse(activeTopic, input)
                    val pronAnalysis = getPronunciationAnalysis(input)
                    val reply = "$pronAnalysis\n\n$rawReply"
                    
                    // Save User's Message
                    tutorChatDao.insertMessage(
                        TutorChatEntity(
                            scenarioId = "partner_" + activeTopic.id,
                            sender = "user",
                            messageText = input,
                            grammarCorrection = correction,
                            timestamp = System.currentTimeMillis()
                        )
                    )

                    // Save AI's Message
                    tutorChatDao.insertMessage(
                        TutorChatEntity(
                            scenarioId = "partner_" + activeTopic.id,
                            sender = "ai",
                            messageText = reply,
                            grammarCorrection = null,
                            timestamp = System.currentTimeMillis()
                        )
                    )

                    speak(reply)
                    isLoadingGemini = false
                }
            }
        }
    }

    // Handle input evaluation
    fun evaluateInput(input: String) {
        val trimmed = input.trim()
        if (trimmed.isBlank() || trimmed == "Listening...") return
        
        userSpokenText = trimmed
        if (isGeminiMode) {
            evaluateWithGemini(trimmed)
        } else {
            isLoadingGemini = true
            coroutineScope.launch {
                // Simulate quick offline calculation delay
                withContext(Dispatchers.Default) {
                    Thread.sleep(600)
                }
                val (rawReply, correction) = getOfflineResponse(activeTopic, trimmed)
                val pronAnalysis = getPronunciationAnalysis(trimmed)
                val reply = "$pronAnalysis\n\n$rawReply"

                // Save User's Message
                tutorChatDao.insertMessage(
                    TutorChatEntity(
                        scenarioId = "partner_" + activeTopic.id,
                        sender = "user",
                        messageText = trimmed,
                        grammarCorrection = correction,
                        timestamp = System.currentTimeMillis()
                    )
                )

                // Save AI's Message
                tutorChatDao.insertMessage(
                    TutorChatEntity(
                        scenarioId = "partner_" + activeTopic.id,
                        sender = "ai",
                        messageText = reply,
                        grammarCorrection = null,
                        timestamp = System.currentTimeMillis()
                    )
                )

                speak(reply)
                isLoadingGemini = false
            }
        }
    }

    // Speech Recognizer setup
    var isListening by remember { mutableStateOf(false) }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ur-PK") // listen to Urdu/English blends
            putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, arrayListOf("en-US", "ur-PK"))
        }
    }

    // Speech Listener callback
    val listener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                userSpokenText = "Listening..."
            }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                isListening = false
            }
            override fun onError(error: Int) {
                isListening = false
                userSpokenText = "Could not hear clearly. Try again!"
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val text = matches[0]
                    userSpokenText = text
                    evaluateInput(text)
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    LaunchedEffect(Unit) {
        speechRecognizer.setRecognitionListener(listener)
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }

    if (showTopicSelection) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "AI Chat Partner",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Header card with XP display
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ElectricViolet.copy(alpha = 0.08f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    brush = Brush.linearGradient(listOf(SunsetAmber, ElectricViolet)),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👩‍🏫", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Select a Topic to Start",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "روزانہ کی گپ شپ کے لیے ایک موضوع کا انتخاب کریں",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // XP Chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SunsetAmber.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.3f)),
                            modifier = Modifier.testTag("user_xp_chip")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("⚡", fontSize = 14.sp)
                                Text(
                                    text = "$userXp XP",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = SunsetAmber
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(DailyTopic.values()) { topic ->
                        val isCompleted = remember(topic, showTopicSelection) {
                            topicPrefs.getBoolean("topic_completed_${topic.id}", false)
                        }
                        val completedDate = remember(topic, showTopicSelection) {
                            topicPrefs.getString("topic_date_${topic.id}", "")
                        }

                        Card(
                            onClick = { 
                                activeTopic = topic 
                                showTopicSelection = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("topic_card_${topic.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (isCompleted) Color(0xFF2E7D32).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(if (isCompleted) Color(0xFF2E7D32).copy(alpha = 0.1f) else ElectricViolet.copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = topic.icon, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = topic.title,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            if (isCompleted) {
                                                Surface(
                                                    color = Color(0xFF2E7D32).copy(alpha = 0.1f),
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.testTag("completed_badge_${topic.id}")
                                                ) {
                                                    Text(
                                                        text = "Completed",
                                                        color = Color(0xFF2E7D32),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = topic.urduTitle,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                fontStyle = FontStyle.Italic
                                            ),
                                            color = if (isCompleted) Color(0xFF2E7D32) else ElectricViolet
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = topic.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    if (isCompleted && !completedDate.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Done on $completedDate",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                            color = Color(0xFF2E7D32)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Start practice",
                                    tint = if (isCompleted) Color(0xFF2E7D32) else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    brush = Brush.linearGradient(listOf(SunsetAmber, ElectricViolet)),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👩‍🏫", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "English Teacher",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isGeminiMode) NeonPink.copy(alpha = 0.15f) else SunsetAmber.copy(alpha = 0.15f),
                                ) {
                                    Text(
                                        text = if (isGeminiMode) "AI Live 🤖" else "Tutor Mode 📚",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                        color = if (isGeminiMode) NeonPink else SunsetAmber,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showTopicSelection = true }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Reset Button
                    IconButton(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                tutorChatDao.clearScenarioChat("partner_" + activeTopic.id)
                                // Re-insert initial message
                                tutorChatDao.insertMessage(
                                    TutorChatEntity(
                                        scenarioId = "partner_" + activeTopic.id,
                                        sender = "ai",
                                        messageText = activeTopic.initialEn + " / " + activeTopic.initialUr,
                                        grammarCorrection = null,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                                withContext(Dispatchers.Main) {
                                    userSpokenText = ""
                                    speak(activeTopic.initialEn)
                                }
                            }
                        },
                        modifier = Modifier.testTag("reset_chat_history")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset chat history")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Horizontal Topics Row Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DailyTopic.values().forEach { topic ->
                    val isSelected = activeTopic == topic
                    Surface(
                        onClick = { activeTopic = topic },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) ElectricViolet else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) ElectricViolet else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier
                            .width(105.dp)
                            .testTag("topic_chip_${topic.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = topic.icon, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = topic.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.sp
                                ),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Topic Description & Level Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "🎯 Active Topic: ${activeTopic.description}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                val currentQNum = (chatMessages.count { it.sender == "user" } + 1).coerceAtMost(activeTopic.questions.size)
                val (levelStr, levelColor) = when {
                    currentQNum <= 5 -> "Level 1: Basic" to Color(0xFF2E7D32)
                    currentQNum <= 10 -> "Level 2: Intermediate" to SunsetAmber
                    else -> "Level 3: Advanced & Roleplay" to ElectricViolet
                }
                
                Surface(
                    color = levelColor.copy(alpha = 0.08f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, levelColor.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("progress_bar_container")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Question $currentQNum of ${activeTopic.questions.size}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("progress_question_label")
                            )
                            Text(
                                text = levelStr,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = levelColor,
                                modifier = Modifier.testTag("progress_level_label")
                            )
                        }
                        
                        LinearProgressIndicator(
                            progress = currentQNum.toFloat() / activeTopic.questions.size.toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .testTag("progress_bar_indicator"),
                            color = levelColor,
                            trackColor = levelColor.copy(alpha = 0.15f)
                        )
                    }
                }
            }

            // Middle: Chat Message Timeline
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(chatMessages) { message ->
                        val isUser = message.sender == "user"
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            if (!isUser) {
                                // AI Avatar alongside message
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(SunsetAmber.copy(alpha = 0.15f), CircleShape)
                                        .align(Alignment.Top),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "👩‍🏫", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Column(
                                modifier = Modifier.widthIn(max = 290.dp)
                            ) {
                                // Split text into English and Urdu parts
                                val textParts = message.messageText.split(" / ")
                                val englishText = textParts.firstOrNull() ?: message.messageText
                                val urduText = if (textParts.size > 1) textParts[1] else null

                                // Main Chat Bubble Card
                                Card(
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isUser) 16.dp else 2.dp,
                                        bottomEnd = if (isUser) 2.dp else 16.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isUser) {
                                            Color(0xFFE8F5E9) // Beautiful Light Green for User
                                        } else {
                                            Color(0xFFE3F2FD) // Beautiful Light Blue for AI Teacher
                                        }
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = englishText,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                lineHeight = 20.sp
                                            ),
                                            color = if (isUser) Color(0xFF1B5E20) else Color(0xFF0D47A1)
                                        )

                                        if (urduText != null && urduText.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = urduText,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontStyle = FontStyle.Italic,
                                                    lineHeight = 21.sp
                                                ),
                                                color = if (isUser) Color(0xFF2E7D32) else Color(0xFF1565C0)
                                            )
                                        }

                                        // Volume speak button on AI card
                                        if (!isUser) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.VolumeUp,
                                                    contentDescription = "Speak this message",
                                                    tint = Color(0xFF1565C0),
                                                    modifier = Modifier
                                                        .size(18.dp)
                                                        .clickable { speak(englishText) }
                                                )
                                            }
                                        }
                                    }
                                }

                                // Grammar Correction Sub-Card (Displayed directly under User messages if they had mistakes)
                                if (isUser && message.grammarCorrection != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Card(
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)), // Soft warm yellow warning card
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFBC02D)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Lightbulb,
                                                contentDescription = "Grammar Correction",
                                                tint = Color(0xFFF57F17),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(
                                                    text = "Teacher's Tip:",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Color(0xFFF57F17)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = message.grammarCorrection,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontSize = 11.sp,
                                                        lineHeight = 14.sp
                                                    ),
                                                    color = Color(0xFF5D4037)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (isUser) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(ElectricViolet.copy(alpha = 0.15f), CircleShape)
                                        .align(Alignment.Top),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "👤", fontSize = 16.sp)
                                }
                            }
                        }
                    }

                    // Typing Indicator inside list
                    if (isLoadingGemini) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(SunsetAmber.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "👩‍🏫", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            color = ElectricViolet,
                                            modifier = Modifier.size(14.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Teacher is thinking...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Console: AUTO-EVALUATOR SIMULATOR TRYOUT BUTTONS
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🤖 Simulator Console (Instant Test Speech)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val stepIndex = chatMessages.count { it.sender == "user" }
                        val simulationOptions = when (activeTopic) {
                            DailyTopic.ROUTINE -> {
                                when (stepIndex) {
                                    0 -> listOf("Main 7 baje uthta hun", "I wake up 7 oclock", "I wake up at 7 AM")
                                    1 -> listOf("Maine nashta kiya", "I eat breakfast egg", "I eat bread for breakfast")
                                    2 -> listOf("Main college parhta hoon", "I working in office", "I study at college")
                                    3 -> listOf("Shaam ko walk karta hoon", "I go walk in evening", "I watch TV with my family")
                                    else -> listOf("Main 11 baje sota hoon", "I sleep 11", "I go to sleep at 11 PM")
                                }
                            }
                            DailyTopic.INTERVIEW -> {
                                when (stepIndex) {
                                    0 -> listOf("Mera naam Ali hai", "I am a mobile developer", "I love designing apps")
                                    1 -> listOf("Main hard working hoon", "I am very punctual", "I speak good English")
                                    2 -> listOf("I want to learn here", "Mujhe paisay chahiye", "I like your company culture")
                                    3 -> listOf("I won best student award", "Maine company ka revenue barhaya", "Nothing special")
                                    else -> listOf("I want to be a manager", "As a senior leader", "In a stable position")
                                }
                            }
                            DailyTopic.SHOPPING -> listOf("I want to buy shoes", "This color is black", "I will pay with cash")
                            DailyTopic.DOCTOR -> listOf("Mujhe bukhar hai", "Since two days", "I don't have headache")
                            DailyTopic.TRAVEL -> listOf("I want to go northern areas", "I prefer window seat", "I have two bags")
                            DailyTopic.RESTAURANT -> listOf("Maine biryani khani hai", "I want hot soup", "I prefer medium spicy")
                            DailyTopic.FRIENDS_FAMILY -> listOf("Mera dost Ali hai", "We celebrate with cake", "I live in a small apartment")
                            DailyTopic.STUDY -> listOf("Mujhe English pasand hai", "I study 4 hours daily", "I like physical books")
                            DailyTopic.WEATHER -> listOf("Aaj bohot garmi hai", "I love rainy days", "I read news online")
                            DailyTopic.SMALL_TALK -> listOf("Thank you very much", "I spend Sunday relaxing", "I want flying power")
                        }

                        simulationOptions.forEach { simText ->
                            Surface(
                                onClick = { evaluateInput(simText) },
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.3f)),
                                modifier = Modifier.testTag("simulator_btn_${simText.replace(" ", "_")}")
                            ) {
                                Text(
                                    text = simText,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                    color = ElectricViolet,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Controller: Microphone Dock with "Hold to Speak"
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(110.dp)
                    ) {
                        // Pulse circle animations when recording
                        if (isListening) {
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse_mic")
                            val scale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = 1.4f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "scale"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFE8F5E9), CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(Color(0xFFC8E6C9), CircleShape)
                            )
                        }

                        // Big Mic Button. Supports both Tap (toggle) and Hold (pointerInput).
                        Button(
                            onClick = {},
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isListening) Color(0xFF4CAF50) else ElectricViolet
                            ),
                            modifier = Modifier
                                .size(72.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            if (!hasAudioPermission) {
                                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            } else {
                                                try {
                                                    isListening = true
                                                    speechRecognizer.startListening(speechIntent)
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }
                                                tryAwaitRelease()
                                                isListening = false
                                                speechRecognizer.stopListening()
                                            }
                                        },
                                        onTap = {
                                            if (!hasAudioPermission) {
                                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            } else {
                                                if (isListening) {
                                                    isListening = false
                                                    speechRecognizer.stopListening()
                                                } else {
                                                    isListening = true
                                                    try {
                                                        speechRecognizer.startListening(speechIntent)
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                        isListening = false
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                                .testTag("big_mic_button"),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = "Hold to speak button",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isListening) "Listening... Release to evaluate" else "Hold to Speak (or Tap)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isListening) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { showCompletionDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(SunsetAmber.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 40.sp)
                }
            },
            title = {
                Text(
                    text = "🎉 Topic Completed! +100 XP",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Great job! You have successfully answered all ${activeTopic.questions.size} questions of this conversation topic.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SunsetAmber.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("⚡", fontSize = 16.sp)
                            Text(
                                text = "+100 XP Earned!",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SunsetAmber
                            )
                        }
                    }

                    if (pastTenseMistakeCount >= 3) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("past_tense_tip_card"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("💡", fontSize = 20.sp)
                                Text(
                                    text = "Tip: You had trouble with Past Tense. Practice it in Grammar Section",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCompletionDialog = false
                        showFeedbackReportDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("view_feedback_report_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricViolet
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View Your Feedback Report 📊", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            },
            dismissButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = {
                            showCompletionDialog = false
                            val values = DailyTopic.values()
                            val currentIndex = values.indexOf(activeTopic)
                            val next = if (currentIndex != -1 && currentIndex < values.size - 1) {
                                values[currentIndex + 1]
                            } else {
                                values[0]
                            }
                            activeTopic = next
                            showTopicSelection = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("next_topic_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Next Topic: ${nextTopic.title}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(
                        onClick = { 
                            showCompletionDialog = false
                            showTopicSelection = true 
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Back to Topics Selection",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        )
    }

    if (showFeedbackReportDialog) {
        val userMessages = remember(chatMessages) { chatMessages.filter { it.sender == "user" } }
        val messagesWithCorrections = remember(chatMessages) { userMessages.filter { !it.grammarCorrection.isNullOrBlank() } }
        val totalQCount = activeTopic.questions.size
        val scoreNumber = (totalQCount - messagesWithCorrections.size).coerceIn(0, totalQCount)

        AlertDialog(
            onDismissRequest = { showFeedbackReportDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(ElectricViolet.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📊", fontSize = 36.sp)
                }
            },
            title = {
                Text(
                    text = "Your Feedback Report",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("feedback_report_title")
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("feedback_report_content"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Analysis of all $totalQCount answers in ${activeTopic.title}:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // 1. Score: 12/15
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("feedback_score_card"),
                        shape = RoundedCornerShape(12.dp),
                        color = ElectricViolet.copy(alpha = 0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("🎯", fontSize = 24.sp)
                                Column {
                                    Text(
                                        text = "Performance Score",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Score: $scoreNumber/$totalQCount",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = ElectricViolet,
                                        modifier = Modifier.testTag("feedback_score_text")
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ElectricViolet.copy(alpha = 0.18f)
                            ) {
                                Text(
                                    text = if (scoreNumber >= 12) "Excellent" else "Keep Going",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = ElectricViolet,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // 2. 1 Strength
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("feedback_strength_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("💪", fontSize = 18.sp)
                                Text(
                                    text = "1 Strength",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF1B5E20)
                                )
                            }
                            Text(
                                text = if (scoreNumber >= 12) 
                                    "Great vocabulary & active participation across all $totalQCount conversation prompts!" 
                                else 
                                    "Consistent engagement, quick confidence, and clear answers during the conversation.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.testTag("feedback_strength_text")
                            )
                        }
                    }

                    // 3. 1 Area to Improve
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("feedback_area_to_improve_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SunsetAmber.copy(alpha = 0.12f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("💡", fontSize = 18.sp)
                                Text(
                                    text = "1 Area to Improve",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFD84315)
                                )
                            }
                            Text(
                                text = if (pastTenseMistakeCount >= 2 || messagesWithCorrections.isNotEmpty())
                                    "Past tense verb consistency & preposition usage in extended conversational answers."
                                else
                                    "Joining complex sentences using conjunctions and expanded vocabulary.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFBF360C),
                                modifier = Modifier.testTag("feedback_area_to_improve_text")
                            )
                        }
                    }
                }
            },
            confirmButton = {
                // 4. Button: "Practice Weak Area in Grammar"
                Button(
                    onClick = {
                        showFeedbackReportDialog = false
                        if (onNavigateToGrammar != null) {
                            onNavigateToGrammar()
                        } else {
                            showTopicSelection = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("practice_weak_area_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricViolet
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Practice Weak Area in Grammar",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showFeedbackReportDialog = false
                        val values = DailyTopic.values()
                        val currentIndex = values.indexOf(activeTopic)
                        val next = if (currentIndex != -1 && currentIndex < values.size - 1) {
                            values[currentIndex + 1]
                        } else {
                            values[0]
                        }
                        activeTopic = next
                        showTopicSelection = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Next Topic: ${nextTopic.title}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}
