package com.example.data.model

enum class TenseCategory(val displayName: String, val urduName: String) {
    PRESENT("Present Tenses", "زمانہ حال"),
    PAST("Past Tenses", "زمانہ ماضی"),
    FUTURE("Future Tenses", "زمانہ مستقبل")
}

data class Formula(
    val positive: String,
    val negative: String,
    val question: String
)

data class SentenceExample(
    val english: String,
    val urdu: String
)

data class CommonMistake(
    val incorrect: String,
    val correct: String,
    val explanationEn: String,
    val explanationUrdu: String
)

enum class ExerciseType(val label: String) {
    FILL_IN_BLANK("Fill in the Blank"),
    MULTIPLE_CHOICE("Choose Correct Answer"),
    SENTENCE_CORRECTION("Sentence Correction"),
    TRANSLATION_URDU_TO_ENG("Urdu to English"),
    TRANSLATION_ENG_TO_URDU("English to Urdu")
}

data class TenseExercise(
    val id: String,
    val type: ExerciseType,
    val question: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanationUrdu: String,
    val explanationEn: String
)

data class EnglishTense(
    val id: String,
    val name: String,
    val urduName: String,
    val category: TenseCategory,
    val easyEnglishExplanation: String,
    val urduExplanation: String,
    val formula: Formula,
    val positiveSentences: List<SentenceExample>,
    val negativeSentences: List<SentenceExample>,
    val questionSentences: List<SentenceExample>,
    val realLifeExamples: List<SentenceExample>,
    val commonMistakes: List<CommonMistake>,
    val exercises: List<TenseExercise>
)

object TenseDataset {
    val tenses: List<EnglishTense> = listOf(
        // 1. Present Simple
        EnglishTense(
            id = "present_simple",
            name = "Present Simple Tense",
            urduName = "فعل حال سادہ",
            category = TenseCategory.PRESENT,
            easyEnglishExplanation = "Used for habitual actions, daily routines, general truths, and permanent situations.",
            urduExplanation = "یہ زمانہ روزمرہ کی عادتوں، مستقل سچائیوں، عمومی حقائق اور روزانہ کے معمولات کو بیان کرنے کے لیے استعمال ہوتا ہے۔ اردو جملے کے آخر میں 'تا ہے'، 'تی ہے'، 'تے ہیں' آتا ہے۔",
            formula = Formula(
                positive = "Subject + Verb (1st Form) (+ s/es for He/She/It) + Object",
                negative = "Subject + do/does + not + Verb (1st Form) + Object",
                question = "Do/Does + Subject + Verb (1st Form) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("I speak English fluently.", "میں روانی سے انگریزی بولتا ہوں۔"),
                SentenceExample("She drinks tea every morning.", "وہ ہر صبح چائے پیتی ہے۔"),
                SentenceExample("The sun rises in the east.", "سورج مشرق سے نکلتا ہے۔")
            ),
            negativeSentences = listOf(
                SentenceExample("He does not play cricket on Sundays.", "وہ اتوار کو کرکٹ نہیں کھیلتا ہے۔"),
                SentenceExample("They do not eat junk food.", "وہ غیر صحتمند کھانا نہیں کھاتے ہیں۔")
            ),
            questionSentences = listOf(
                SentenceExample("Do you work at the hospital?", "کیا آپ ہسپتال میں کام کرتے ہیں؟"),
                SentenceExample("Does she live in Lahore?", "کیا وہ لاہور میں رہتی ہے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("Water boils at 100 degrees Celsius.", "پانی 100 ڈگری سیلسیس پر ابلتا ہے۔"),
                SentenceExample("Ali teaches Mathematics at university.", "علی یونیورسٹی میں ریاضی پڑھاتا ہے۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "He go to school every day.",
                    correct = "He goes to school every day.",
                    explanationEn = "With singular subjects (He/She/It), add 's' or 'es' to the verb in Present Simple.",
                    explanationUrdu = "واحد فاعل (He, She, It) کے ساتھ ورب کی پہلی فارم کے ساتھ s یا es کا اضافہ کیا جاتا ہے۔"
                ),
                CommonMistake(
                    incorrect = "She does not likes tea.",
                    correct = "She does not like tea.",
                    explanationEn = "After 'does not', use the base verb form without 's/es'.",
                    explanationUrdu = "does not کے بعد ورب کی بنیادی فارم آتی ہے، s یا es کا اضافہ نہیں ہوتا۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "ps_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "She _____ (read) books every night before sleeping.",
                    options = listOf("reads", "reading", "readed", "is read"),
                    correctAnswer = "reads",
                    explanationUrdu = "واحد فاعل 'She' کے ساتھ 'reads' آئے گا۔",
                    explanationEn = "Singular subject 'She' requires verb + s (reads)."
                ),
                TenseExercise(
                    id = "ps_ex2",
                    type = ExerciseType.MULTIPLE_CHOICE,
                    question = "Which sentence is grammatically correct?",
                    options = listOf(
                        "They doesn't play football.",
                        "They don't play football.",
                        "They not play football.",
                        "They does not plays football."
                    ),
                    correctAnswer = "They don't play football.",
                    explanationUrdu = "جمع فاعل 'They' کے ساتھ 'don't' استعمال ہوتا ہے۔",
                    explanationEn = "Plural subject 'They' pairs with 'don't'."
                ),
                TenseExercise(
                    id = "ps_ex3",
                    type = ExerciseType.TRANSLATION_URDU_TO_ENG,
                    question = "Translate into English: 'وہ روزانہ ورزش کرتا ہے۔'",
                    options = listOf(
                        "He exercises daily.",
                        "He is exercise daily.",
                        "He exercising daily.",
                        "He exercise daily."
                    ),
                    correctAnswer = "He exercises daily.",
                    explanationUrdu = "روزمرہ کے معمول کے لیے Present Simple اور He کے ساتھ exercises استعمال ہوتا ہے۔",
                    explanationEn = "Present simple requires 'exercises' for 'He'."
                )
            )
        ),

        // 2. Present Continuous
        EnglishTense(
            id = "present_continuous",
            name = "Present Continuous Tense",
            urduName = "فعل حال جاری",
            category = TenseCategory.PRESENT,
            easyEnglishExplanation = "Describes actions happening right now at the moment of speaking or temporary situations.",
            urduExplanation = "یہ زمانہ ان کاموں کے لیے استعمال ہوتا ہے جو بات کرتے وقت جاری ہوں۔ اردو جملے کے آخر میں 'رہا ہے'، 'رہی ہے'، 'رہے ہیں' آتا ہے۔",
            formula = Formula(
                positive = "Subject + am/is/are + Verb(-ing) + Object",
                negative = "Subject + am/is/are + not + Verb(-ing) + Object",
                question = "Am/Is/Are + Subject + Verb(-ing) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("I am learning English grammar now.", "میں ابھی انگریزی گرامر سیکھ رہا ہوں۔"),
                SentenceExample("She is cooking dinner in the kitchen.", "وہ باورچی خانے میں کھانا پکا رہی ہے۔"),
                SentenceExample("They are playing football in the park.", "وہ پارک میں فٹ بال کھیل رہے ہیں۔")
            ),
            negativeSentences = listOf(
                SentenceExample("He is not watching television right now.", "وہ اس وقت ٹیلی ویژن نہیں دیکھ رہا ہے۔"),
                SentenceExample("We are not wasting our precious time.", "ہم اپنا قیمتی وقت ضائع نہیں کر رہے ہیں۔")
            ),
            questionSentences = listOf(
                SentenceExample("Are you listening to the music?", "کیا آپ موسیقی سن رہے ہیں؟"),
                SentenceExample("Is it raining outside?", "کیا باہر بارش ہو رہی ہے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("Look! The train is coming.", "دیکھو! ٹرین آ رہی ہے۔"),
                SentenceExample("Why are you laughing at him?", "آپ اس پر کیوں ہنس رہے ہیں؟")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "I am agree with your point.",
                    correct = "I agree with your point.",
                    explanationEn = "Stative verbs like 'agree', 'know', 'love' are usually not used in continuous tenses.",
                    explanationUrdu = "احساسات اور خیالات والے افعال (Stative verbs) کو جاری زمانے (Continuous) میں استعمال نہیں کیا جاتا۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "pc_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "Listen! Someone _____ (knock) on the front door.",
                    options = listOf("is knocking", "knocks", "are knocking", "knocked"),
                    correctAnswer = "is knocking",
                    explanationUrdu = "فی الوقت جاری عمل ظاہر کرنے کے لیے 'is knocking' درست ہے۔",
                    explanationEn = "'is knocking' shows an action happening right at this instance."
                )
            )
        ),

        // 3. Present Perfect
        EnglishTense(
            id = "present_perfect",
            name = "Present Perfect Tense",
            urduName = "فعل حال مکمل",
            category = TenseCategory.PRESENT,
            easyEnglishExplanation = "Used for actions completed in the recent past that have a direct connection to the present.",
            urduExplanation = "یہ زمانہ ان کاموں کے لیے استعمال ہوتا ہے جو حال ہی میں مکمل ہوئے ہوں لیکن ان کا اثر حال سے جڑا ہو۔ اردو جملے کے آخر میں 'چکا ہے'، 'چکی ہے'، 'یا ہے'، 'ئے ہیں' آتا ہے۔",
            formula = Formula(
                positive = "Subject + has/have + Verb (3rd Form) + Object",
                negative = "Subject + has/have + not + Verb (3rd Form) + Object",
                question = "Has/Have + Subject + Verb (3rd Form) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("I have completed my homework.", "میں اپنا ہوم ورک مکمل کر چکا ہوں۔"),
                SentenceExample("She has lived in Islamabad for 5 years.", "وہ 5 سال سے اسلام آباد میں رہ چکی ہے۔"),
                SentenceExample("They have won the championship match.", "وہ چیمپئن شپ میچ جیت چکے ہیں۔")
            ),
            negativeSentences = listOf(
                SentenceExample("I have not seen that movie yet.", "میں نے ابھی تک وہ فلم نہیں دیکھی ہے۔"),
                SentenceExample("He has not finished his lunch.", "اس نے اپنا دوپہر کا کھانا ختم نہیں کیا ہے۔")
            ),
            questionSentences = listOf(
                SentenceExample("Have you ever visited northern Pakistan?", "کیا آپ نے کبھی شمالی پاکستان کی سیر کی ہے؟"),
                SentenceExample("Has she sent the email report?", "کیا اس نے ای میل رپورٹ بھیج دی ہے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("We have just arrived at the airport.", "ہم ابھی ابھی ہوائی اڈے پر پہنچے ہیں۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "I have seen him yesterday.",
                    correct = "I saw him yesterday.",
                    explanationEn = "Do not use specific past time adverbs (yesterday, last year) with Present Perfect. Use Past Simple instead.",
                    explanationUrdu = "مخصوص ماضی کے وقت (کل، گزشتہ سال) کے ساتھ Present Perfect کے بجائے Past Simple کا استعمال ہوتا ہے۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "pp_ex1",
                    type = ExerciseType.MULTIPLE_CHOICE,
                    question = "He _____ his keys, so he cannot enter the house.",
                    options = listOf("has lost", "losts", "is losing", "have lost"),
                    correctAnswer = "has lost",
                    explanationUrdu = "'He' کے ساتھ 'has' اور 3rd form 'lost' لگے گی۔",
                    explanationEn = "Singular 'He' uses 'has' + 3rd form 'lost'."
                )
            )
        ),

        // 4. Present Perfect Continuous
        EnglishTense(
            id = "present_perfect_continuous",
            name = "Present Perfect Continuous Tense",
            urduName = "فعل حال مکمل جاری",
            category = TenseCategory.PRESENT,
            easyEnglishExplanation = "Focuses on the duration of an action that started in the past and is still continuing in the present.",
            urduExplanation = "یہ زمانہ ان کاموں کے لیے استعمال ہوتا ہے جو ماضی میں شروع ہوئے ہوں اور اب بھی جاری ہوں۔ اس میں وقت کا اشارہ (Since/For) پایا جاتا ہے۔",
            formula = Formula(
                positive = "Subject + has/have + been + Verb(-ing) + Object + since/for + Time",
                negative = "Subject + has/have + not + been + Verb(-ing) + Object + since/for + Time",
                question = "Has/Have + Subject + been + Verb(-ing) + Object + since/for + Time?"
            ),
            positiveSentences = listOf(
                SentenceExample("It has been raining since morning.", "صبح سے بارش ہو رہی ہے۔"),
                SentenceExample("I have been studying for three hours.", "میں تین گھنٹے سے پڑھ رہا ہوں۔")
            ),
            negativeSentences = listOf(
                SentenceExample("She has not been feeling well since yesterday.", "وہ کل سے بہتر محسوس نہیں کر رہی ہے۔")
            ),
            questionSentences = listOf(
                SentenceExample("How long have you been waiting here?", "آپ یہاں کتنی دیر سے انتظار کر رہے ہیں؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("Ali has been working on this AI project for two months.", "علی دو ماہ سے اس AI پروجیکٹ پر کام کر رہا ہے۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "It is raining since morning.",
                    correct = "It has been raining since morning.",
                    explanationEn = "Use 'has been raining' instead of 'is raining' when specifying time with 'since' or 'for'.",
                    explanationUrdu = "جب وقت کا ذکر (since/for) ہو تو Present Continuous کے بجائے Present Perfect Continuous استعمال کریں۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "ppc_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "They have been playing cricket _____ 2 o'clock.",
                    options = listOf("since", "for", "from", "in"),
                    correctAnswer = "since",
                    explanationUrdu = "مخصوص نقطہ وقت (2 o'clock) کے لیے 'since' استعمال ہوتا ہے۔",
                    explanationEn = "'since' is used for a specific starting point of time."
                )
            )
        ),

        // 5. Past Simple
        EnglishTense(
            id = "past_simple",
            name = "Past Simple Tense",
            urduName = "فعل ماضی سادہ",
            category = TenseCategory.PAST,
            easyEnglishExplanation = "Used for completed actions at a specific time in the past.",
            urduExplanation = "یہ زمانہ ماضی میں مکمل ہونے والے کسی کام کو ظاہر کرنے کے لیے استعمال ہوتا ہے۔ جملے کے آخر میں 'یا'، 'ئی'، 'ئے'، 'تھا'، 'تھی' آتا ہے۔",
            formula = Formula(
                positive = "Subject + Verb (2nd Form) + Object",
                negative = "Subject + did + not + Verb (1st Form) + Object",
                question = "Did + Subject + Verb (1st Form) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("I bought a new phone yesterday.", "میں نے کل نیا فون خریدا۔"),
                SentenceExample("She visited Karachi last week.", "اس نے گزشتہ ہفتے کراچی کی سیر کی تھی۔")
            ),
            negativeSentences = listOf(
                SentenceExample("They did not attend the meeting.", "انہوں نے اجلاس میں شرکت نہیں کی۔")
            ),
            questionSentences = listOf(
                SentenceExample("Did you complete the assignment?", "کیا آپ نے اسائنمنٹ مکمل کی؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("Shakespeare wrote many famous plays.", "شیکسپیئر نے کئی مشہور ڈرامے لکھے۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "She did not went to school.",
                    correct = "She did not go to school.",
                    explanationEn = "Always use the 1st form of verb after 'did' or 'did not'.",
                    explanationUrdu = "'did' یا 'did not' کے بعد ہمیشہ ورب کی پہلی فارم استعمال ہوتی ہے۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "pas_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "We _____ (see) a wonderful movie last night.",
                    options = listOf("saw", "seen", "seeing", "sees"),
                    correctAnswer = "saw",
                    explanationUrdu = "ماضی کے کام کے لیے 2nd form 'saw' آئے گی۔",
                    explanationEn = "Past simple positive sentences require 2nd form of verb (saw)."
                )
            )
        ),

        // 6. Past Continuous
        EnglishTense(
            id = "past_continuous",
            name = "Past Continuous Tense",
            urduName = "فعل ماضی جاری",
            category = TenseCategory.PAST,
            easyEnglishExplanation = "Describes actions that were actively happening at a specific moment in the past.",
            urduExplanation = "یہ زمانہ ماضی کے کسی خاص وقت میں جاری کاموں کو ظاہر کرنے کے لیے استعمال ہوتا ہے۔ جملے کے آخر میں 'رہا تھا'، 'رہی تھی'، 'رہے تھے' آتا ہے۔",
            formula = Formula(
                positive = "Subject + was/were + Verb(-ing) + Object",
                negative = "Subject + was/were + not + Verb(-ing) + Object",
                question = "Was/Were + Subject + Verb(-ing) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("I was reading a book when he arrived.", "جب وہ آیا تو میں کتاب پڑھ رہا تھاے۔"),
                SentenceExample("They were sleeping soundly at midnight.", "وہ آدھی رات کو گہری نیند سو رہے تھے۔")
            ),
            negativeSentences = listOf(
                SentenceExample("She was not driving fast.", "وہ تیز گاڑی نہیں چلا رہی تھی۔")
            ),
            questionSentences = listOf(
                SentenceExample("Were you studying at 9 PM?", "کیا آپ رات 9 بجے پڑھ رہے تھے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("The birds were singing beautifully at sunrise.", "طلوع آفتاب کے وقت پرندے خوبصورتی سے چہچہا رہے تھے۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "They was playing football.",
                    correct = "They were playing football.",
                    explanationEn = "Use 'were' with plural subjects (They, We, You).",
                    explanationUrdu = "جمع فاعل (They, We, You) کے ساتھ 'were' کا استعمال کیا جاتا ہے۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "pac_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "While I was walking in the park, it _____ (start) to rain.",
                    options = listOf("started", "was starting", "is starting", "starts"),
                    correctAnswer = "started",
                    explanationUrdu = "جاری عمل کے دوران اچانک ہونے والے عمل کے لیے Past Simple (started) استعمال ہوتا ہے۔",
                    explanationEn = "Interrupting past action takes Past Simple (started)."
                )
            )
        ),

        // 7. Past Perfect
        EnglishTense(
            id = "past_perfect",
            name = "Past Perfect Tense",
            urduName = "فعل ماضی مکمل",
            category = TenseCategory.PAST,
            easyEnglishExplanation = "Used to show an action completed before another past event occurred.",
            urduExplanation = "یہ زمانہ ماضی میں کسی دوسرے کام سے پہلے مکمل ہو جانے والے کام کو ظاہر کرتا ہے۔ جملے کے آخر میں 'چکا تھا'، 'چکی تھی'، 'چکے تھے' آتا ہے۔",
            formula = Formula(
                positive = "Subject + had + Verb (3rd Form) + Object",
                negative = "Subject + had + not + Verb (3rd Form) + Object",
                question = "Had + Subject + Verb (3rd Form) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("The train had left before we reached the station.", "ہمارے اسٹیشن پہنچنے سے پہلے ٹرین جا چکی تھی۔"),
                SentenceExample("She had already cooked dinner.", "وہ پہلے ہی کھانا پکا چکی تھی۔")
            ),
            negativeSentences = listOf(
                SentenceExample("He had not seen the movie prior to yesterday.", "اس نے کل سے پہلے یہ فلم نہیں دیکھی تھی۔")
            ),
            questionSentences = listOf(
                SentenceExample("Had you finished the task before the deadline?", "کیا آپ نے ڈیڈ لائن سے پہلے کام مکمل کر لیا تھا؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("By the time the doctor arrived, the patient had recovered.", "ڈاکٹر کے آنے تک مریض صحت یاب ہو چکا تھا۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "I had went to Lahore yesterday.",
                    correct = "I went to Lahore yesterday.",
                    explanationEn = "Use Past Simple for a single past event. Use Past Perfect only when comparing two past events.",
                    explanationUrdu = "ماضی کے صرف ایک واقعے کے لیے Past Simple استعمال کریں، Past Perfect صرف دو واقعات کے تقابل میں آتا ہے۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "pap_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "When we arrived at the hall, the meeting _____ already started.",
                    options = listOf("had", "has", "was", "did"),
                    correctAnswer = "had",
                    explanationUrdu = "ماضی کے پہلے مکمل عمل کے لیے 'had' لگے گا۔",
                    explanationEn = "Earlier past action uses 'had' + 3rd form."
                )
            )
        ),

        // 8. Past Perfect Continuous
        EnglishTense(
            id = "past_perfect_continuous",
            name = "Past Perfect Continuous Tense",
            urduName = "فعل ماضی مکمل جاری",
            category = TenseCategory.PAST,
            easyEnglishExplanation = "Shows an action that was going on for a duration before a point of time in the past.",
            urduExplanation = "یہ زمانہ ان کاموں کے لیے استعمال ہوتا ہے جو ماضی میں ایک عرصے تک جاری رہے اور ماضی کے ہی کسی موڑ پر ختم ہوئے۔ اس میں بھی وقت (since/for) کا ذکر ہوتا ہے۔",
            formula = Formula(
                positive = "Subject + had + been + Verb(-ing) + Object + since/for + Time",
                negative = "Subject + had + not + been + Verb(-ing) + Object + since/for + Time",
                question = "Had + Subject + been + Verb(-ing) + Object + since/for + Time?"
            ),
            positiveSentences = listOf(
                SentenceExample("He had been working for 5 hours before taking a break.", "وقفہ لینے سے پہلے وہ 5 گھنٹے سے کام کر رہا تھا۔")
            ),
            negativeSentences = listOf(
                SentenceExample("They had not been practicing regularly before the tournament.", "ٹورنامنٹ سے پہلے وہ باقاعدگی سے پریکٹس نہیں کر رہے تھے۔")
            ),
            questionSentences = listOf(
                SentenceExample("Had she been waiting long when you arrived?", "جب آپ پہنچے تو کیا وہ کافی دیر سے انتظار کر رہی تھی؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("The ground was wet because it had been raining for hours.", "زمین گیلی تھی کیونکہ گھنٹوں سے بارش ہو رہی تھی۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "I had been knowing him for 10 years.",
                    correct = "I had known him for 10 years.",
                    explanationEn = "Do not use continuous forms with stative verbs like 'know'. Use Past Perfect instead.",
                    explanationUrdu = "'know' جیسے ورب کو continuous فارم میں استعمال نہ کریں۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "papc_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "She was tired because she _____ running for two hours.",
                    options = listOf("had been", "has been", "was", "is"),
                    correctAnswer = "had been",
                    explanationUrdu = "ماضی کے طویل جاری عمل کے لیے 'had been' آئے گا۔",
                    explanationEn = "Past ongoing action duration requires 'had been'."
                )
            )
        ),

        // 9. Future Simple
        EnglishTense(
            id = "future_simple",
            name = "Future Simple Tense",
            urduName = "فعل مستقبل سادہ",
            category = TenseCategory.FUTURE,
            easyEnglishExplanation = "Used for actions that will take place in the future, predictions, or sudden decisions.",
            urduExplanation = "یہ زمانہ آنے والے وقت (مستقبل) میں ہونے والے کاموں کو بیان کرنے کے لیے استعمال ہوتا ہے۔ جملے کے آخر میں 'گا'، 'گی'، 'گے' آتا ہے۔",
            formula = Formula(
                positive = "Subject + will/shall + Verb (1st Form) + Object",
                negative = "Subject + will/shall + not + Verb (1st Form) + Object",
                question = "Will/Shall + Subject + Verb (1st Form) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("I will call you tomorrow morning.", "میں آپ کو کل صبح فون کروں گا۔"),
                SentenceExample("She will pass the exam with high marks.", "وہ اچھے نمبروں سے امتحان پاس کرے گی۔")
            ),
            negativeSentences = listOf(
                SentenceExample("We will not tolerate dishonesty.", "ہم بددیانتی برداشت نہیں کریں گے۔")
            ),
            questionSentences = listOf(
                SentenceExample("Will you help me with this task?", "کیا آپ اس کام میں میری مدد کریں گے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("Technology will transform our daily lives in the coming decade.", "ٹیکنالوجی آنے والی دہائی میں ہماری زندگیوں کو بدل دے گی۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "If it will rain, I will stay home.",
                    correct = "If it rains, I will stay home.",
                    explanationEn = "In conditional clauses starting with 'if', use Present Simple instead of 'will'.",
                    explanationUrdu = "اگر (if) والی شرطیہ شق میں 'will' کے بجائے Present Simple استعمال ہوتا ہے۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "fus_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "I think our team _____ (win) tomorrow's match.",
                    options = listOf("will win", "won", "wins", "is win"),
                    correctAnswer = "will win",
                    explanationUrdu = "مستقبل کی پیشگوئی کے لیے 'will win' استعمال ہوگا۔",
                    explanationEn = "Future prediction uses 'will + 1st form'."
                )
            )
        ),

        // 10. Future Continuous
        EnglishTense(
            id = "future_continuous",
            name = "Future Continuous Tense",
            urduName = "فعل مستقبل جاری",
            category = TenseCategory.FUTURE,
            easyEnglishExplanation = "Describes an action that will be happening at a specific time in the future.",
            urduExplanation = "یہ زمانہ مستقبل میں کسی خاص وقت پر جاری رہنے والے کام کو ظاہر کرنے کے لیے استعمال ہوتا ہے۔ جملے کے آخر میں 'رہا ہوگا'، 'رہی ہوگی'، 'رہے ہوں گے' آتا ہے۔",
            formula = Formula(
                positive = "Subject + will be + Verb(-ing) + Object",
                negative = "Subject + will + not + be + Verb(-ing) + Object",
                question = "Will + Subject + be + Verb(-ing) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("This time tomorrow, I will be flying to Dubai.", "کل اس وقت میں دبئی کی پرواز پر ہوں گا۔"),
                SentenceExample("They will be celebrating their victory tonight.", "وہ آج رات اپنی فتح کا جشن منا رہے ہوں گے۔")
            ),
            negativeSentences = listOf(
                SentenceExample("She will not be working late this evening.", "وہ آج شام دیر تک کام نہیں کر رہی ہوگی۔")
            ),
            questionSentences = listOf(
                SentenceExample("Will you be attending the annual conference?", "کیا آپ سالانہ کانفرنس میں شرکت کر رہے ہوں گے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("At 8 PM, we will be watching the cricket match live.", "رات 8 بجے ہم کرکٹ میچ براہ راست دیکھ رہے ہوں گے۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "He will be go to office.",
                    correct = "He will be going to office.",
                    explanationEn = "After 'will be', use the verb + -ing form.",
                    explanationUrdu = "'will be' کے بعد ورب کے ساتھ -ing لگتا ہے۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "fuc_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "At 10 AM tomorrow, she _____ (take) an English test.",
                    options = listOf("will be taking", "will take", "takes", "took"),
                    correctAnswer = "will be taking",
                    explanationUrdu = "مستقبل کے طے شدہ جاری وقت کے لیے 'will be taking' آئے گا۔",
                    explanationEn = "Specific future point in time takes Future Continuous."
                )
            )
        ),

        // 11. Future Perfect
        EnglishTense(
            id = "future_perfect",
            name = "Future Perfect Tense",
            urduName = "فعل مستقبل مکمل",
            category = TenseCategory.FUTURE,
            easyEnglishExplanation = "Used for an action that will be completed before a specified point of time in the future.",
            urduExplanation = "یہ زمانہ ان کاموں کے لیے استعمال ہوتا ہے جو مستقبل میں کسی خاص وقت یا واقعے سے پہلے مکمل ہو چکے ہوں گے۔ جملے کے آخر میں 'چکا ہوگا'، 'چکی ہوگی'، 'چکے ہوں گے' آتا ہے۔",
            formula = Formula(
                positive = "Subject + will have + Verb (3rd Form) + Object",
                negative = "Subject + will + not + have + Verb (3rd Form) + Object",
                question = "Will + Subject + have + Verb (3rd Form) + Object?"
            ),
            positiveSentences = listOf(
                SentenceExample("By next month, I will have finished my degree.", "اگلے مہینے تک میں اپنی ڈگری مکمل کر چکا ہوں گا۔"),
                SentenceExample("She will have arrived in London by evening.", "وہ شام تک لندن پہنچ چکی ہوگی۔")
            ),
            negativeSentences = listOf(
                SentenceExample("We will not have completed the building construction by June.", "ہم جون تک عمارت کی تعمیر مکمل نہیں کر چکے ہوں گے۔")
            ),
            questionSentences = listOf(
                SentenceExample("Will they have submitted the project reports by Monday?", "کیا وہ پیر تک پروجیکٹ رپورٹس جمع کروا چکے ہوں گے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("By 2030, scientists will have developed new AI medical cures.", "2030 تک سائنسدان علاج کے نئے AI طریقے دریافت کر چکے ہوں گے۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "By tomorrow, he will has finished the book.",
                    correct = "By tomorrow, he will have finished the book.",
                    explanationEn = "Always use 'will have' regardless of singular or plural subjects (never 'will has').",
                    explanationUrdu = "ہمیشہ 'will have' استعمال ہوتا ہے، چاہے واحد فاعل کیوں نہ ہو ('will has' غلط ہے)۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "fup_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "By 5 PM, they _____ (finish) all their work.",
                    options = listOf("will have finished", "will finish", "finishes", "have finished"),
                    correctAnswer = "will have finished",
                    explanationUrdu = "مستقبل میں وقت کی حد (By 5 PM) کے ساتھ 'will have finished' آئے گا۔",
                    explanationEn = "Future deadline uses 'will have' + 3rd form."
                )
            )
        ),

        // 12. Future Perfect Continuous
        EnglishTense(
            id = "future_perfect_continuous",
            name = "Future Perfect Continuous Tense",
            urduName = "فعل مستقبل مکمل جاری",
            category = TenseCategory.FUTURE,
            easyEnglishExplanation = "Shows an action that will continue up to a specific time in the future.",
            urduExplanation = "یہ زمانہ ان کاموں کے لیے استعمال ہوتا ہے جو مستقبل کے کسی نقطہ نظر تک جاری رہے ہوں گے اور اس میں وقت کا تعین ہوتا ہے۔",
            formula = Formula(
                positive = "Subject + will have been + Verb(-ing) + Object + since/for + Time",
                negative = "Subject + will + not + have been + Verb(-ing) + Object + since/for + Time",
                question = "Will + Subject + have been + Verb(-ing) + Object + since/for + Time?"
            ),
            positiveSentences = listOf(
                SentenceExample("By next year, I will have been working here for 10 years.", "اگلے سال تک میں یہاں 10 سال سے کام کر رہا ہوں گا۔")
            ),
            negativeSentences = listOf(
                SentenceExample("She will not have been living in Canada for very long by then.", "تب تک وہ کینیڈا میں زیادہ عرصے سے نہیں رہ رہی ہوگی۔")
            ),
            questionSentences = listOf(
                SentenceExample("Will you have been studying for three hours by 8 PM?", "کیا آپ رات 8 بجے تک تین گھنٹے سے پڑھ رہے ہوں گے؟")
            ),
            realLifeExamples = listOf(
                SentenceExample("In December, the team will have been developing the software for two years.", "دسمبر میں ٹیم دو سال سے سافٹ ویئر تیار کر رہی ہوگی۔")
            ),
            commonMistakes = listOf(
                CommonMistake(
                    incorrect = "By 2027, he will be teaching for 5 years.",
                    correct = "By 2027, he will have been teaching for 5 years.",
                    explanationEn = "Use Future Perfect Continuous ('will have been teaching') when specifying time duration in the future.",
                    explanationUrdu = "مستقبل کے وقت اور مدت کا ذکر ہو تو 'will have been' کے ساتھ -ing کا استعمال کریں۔"
                )
            ),
            exercises = listOf(
                TenseExercise(
                    id = "fupc_ex1",
                    type = ExerciseType.FILL_IN_BLANK,
                    question = "By midnight, he _____ sleeping for 8 hours.",
                    options = listOf("will have been", "will be", "is", "has been"),
                    correctAnswer = "will have been",
                    explanationUrdu = "مستقبل میں مدت کے ساتھ جاری عمل کے لیے 'will have been' درست ہے۔",
                    explanationEn = "Future duration ongoing action takes 'will have been'."
                )
            )
        )
    )

    fun getTenseById(id: String): EnglishTense? {
        return tenses.firstOrNull { it.id == id }
    }
}
