package com.hassan.kooged.agents.practiceLanguage.utils

import com.hassan.kooged.agents.practiceLanguage.entities.Message
import kotlinx.serialization.json.Json

object UserMessagesUtils {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    val userMessagesJson = """
        [
          {
            "direction": "outgoing",
            "message": "Hi there! Are you the language exchange partner from the app?",
            "timestamp": "2023-10-27T18:00:00Z"
          },
          {
            "direction": "incoming",
            "message": "Hello! Yes, I am. Nice to meet you. I am looking to improve my English, and I see you want to learn German?",
            "timestamp": "2023-10-27T18:01:15Z"
          },
          {
            "direction": "outgoing",
            "message": "Exactly. Nice to meet you too. I’m planning a trip to Berlin soon and I really need to practice how to survive in a restaurant.",
            "timestamp": "2023-10-27T18:02:05Z"
          },
          {
            "direction": "incoming",
            "message": "That is a great idea. Berlin is wonderful. I can help you with the German phrases, and maybe you can correct my English if I make mistakes?",
            "timestamp": "2023-10-27T18:03:10Z"
          },
          {
            "direction": "outgoing",
            "message": "That sounds like a perfect deal. Shall we start right away? Imagine I just walked into a restaurant.",
            "timestamp": "2023-10-27T18:03:45Z"
          },
          {
            "direction": "incoming",
            "message": "Okay, let's do it. First, you need to greet the waiter. In Germany, we usually say \"Guten Abend\" (Good evening) or just \"Hallo.\"",
            "timestamp": "2023-10-27T18:04:30Z"
          },
          {
            "direction": "outgoing",
            "message": "Okay, easy enough. \"Guten Abend.\" Now, how do I ask for a table for two people?",
            "timestamp": "2023-10-27T18:05:10Z"
          },
          {
            "direction": "incoming",
            "message": "You would say: \"Ein Tisch für zwei Personen, bitte.\"",
            "timestamp": "2023-10-27T18:05:55Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Ein Tisch für zwei Personen, bitte.\" Is that polite enough?",
            "timestamp": "2023-10-27T18:06:20Z"
          },
          {
            "direction": "incoming",
            "message": "Yes, that is polite. Or you can ask: \"Haben Sie einen Tisch für zwei frei?\" which means \"Do you have a table free for two?\"",
            "timestamp": "2023-10-27T18:07:15Z"
          },
          {
            "direction": "outgoing",
            "message": "I like that one better. \"Haben Sie einen Tisch für zwei frei?\"",
            "timestamp": "2023-10-27T18:07:50Z"
          },
          {
            "direction": "incoming",
            "message": "Perfect pronunciation! Now, the waiter will show you the table. Once you sit down, you usually want the menu. Do you know the word for menu?",
            "timestamp": "2023-10-27T18:08:40Z"
          },
          {
            "direction": "outgoing",
            "message": "I think it's \"das Menü\"?",
            "timestamp": "2023-10-27T18:09:10Z"
          },
          {
            "direction": "incoming",
            "message": "Close, but be careful. \"Das Menü\" usually refers to a set meal (like a 3-course meal). The physical card with the list of foods is called \"die Speisekarte.\"",
            "timestamp": "2023-10-27T18:10:05Z"
          },
          {
            "direction": "outgoing",
            "message": "Oh, that’s a really important distinction! So I should say, \"Die Speisekarte, bitte?\"",
            "timestamp": "2023-10-27T18:10:45Z"
          },
          {
            "direction": "incoming",
            "message": "Exactly. Or if you want to be a full sentence: \"Könnte ich bitte die Speisekarte haben?\" (Could I please have the menu?)",
            "timestamp": "2023-10-27T18:11:30Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Könnte ich bitte die Speisekarte haben?\" Got it. Okay, now I have the menu. I want to order a drink first.",
            "timestamp": "2023-10-27T18:12:15Z"
          },
          {
            "direction": "incoming",
            "message": "In Germany, the waiter usually comes quickly for drinks. He might ask: \"Was möchten Sie trinken?\"",
            "timestamp": "2023-10-27T18:13:00Z"
          },
          {
            "direction": "outgoing",
            "message": "I usually just drink water. How do I ask for that without getting sparkling water? I know sparkling is popular there.",
            "timestamp": "2023-10-27T18:13:45Z"
          },
          {
            "direction": "incoming",
            "message": "Yes, if you just say \"Wasser,\" you usually get sparkling (mit Kohlensäure). You must ask for \"stilles Wasser\" (silent/still water).",
            "timestamp": "2023-10-27T18:14:35Z"
          },
          {
            "direction": "outgoing",
            "message": "Okay. \"Ich möchte ein stilles Wasser, bitte.\"",
            "timestamp": "2023-10-27T18:15:05Z"
          },
          {
            "direction": "incoming",
            "message": "Very good. Using \"Ich hätte gerne...\" (I would like...) is even more polite than \"Ich möchte.\" So: \"Ich hätte gerne ein stilles Wasser.\"",
            "timestamp": "2023-10-27T18:15:55Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Ich hätte gerne ein stilles Wasser.\" What if I want a beer?",
            "timestamp": "2023-10-27T18:16:30Z"
          },
          {
            "direction": "incoming",
            "message": "Then you say: \"Ich hätte gerne ein Bier.\" Usually, a Pilsner or a Weizen (wheat beer).",
            "timestamp": "2023-10-27T18:17:10Z"
          },
          {
            "direction": "outgoing",
            "message": "Okay, I’ll stick to the water for now. Now I’m looking at the food options. How do I ask, \"What do you recommend?\"",
            "timestamp": "2023-10-27T18:17:50Z"
          },
          {
            "direction": "incoming",
            "message": "You can ask: \"Was können Sie empfehlen?\"",
            "timestamp": "2023-10-27T18:18:25Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Was können Sie empfehlen?\" Okay. What if I have an allergy? For example, nuts.",
            "timestamp": "2023-10-27T18:19:00Z"
          },
          {
            "direction": "incoming",
            "message": "That is important. You say: \"Ich bin allergisch gegen Nüsse.\" Or ask: \"Sind da Nüsse drin?\" (Are there nuts in there?)",
            "timestamp": "2023-10-27T18:19:50Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Sind da Nüsse drin?\" Useful. What if I want to ask if a dish is spicy?",
            "timestamp": "2023-10-27T18:20:30Z"
          },
          {
            "direction": "incoming",
            "message": "You ask: \"Ist das scharf?\"",
            "timestamp": "2023-10-27T18:21:00Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Ist das scharf?\" Simple. Okay, I’m ready to order my main course. Let’s say I want the Schnitzel.",
            "timestamp": "2023-10-27T18:21:35Z"
          },
          {
            "direction": "incoming",
            "message": "Okay, call the waiter. You can catch their eye and say \"Entschuldigung?\" (Excuse me). Then say: \"Ich würde gerne bestellen.\" (I would like to order).",
            "timestamp": "2023-10-27T18:22:25Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Entschuldigung, ich würde gerne bestellen.\" Then I say: \"Ich nehme das Schnitzel.\"",
            "timestamp": "2023-10-27T18:23:10Z"
          },
          {
            "direction": "incoming",
            "message": "Correct. \"Ich nehme das Schnitzel\" (I'll take the Schnitzel) works well. He might ask \"Mit Pommes oder Salat?\" (With fries or salad?)",
            "timestamp": "2023-10-27T18:24:00Z"
          },
          {
            "direction": "outgoing",
            "message": "I definitely want fries. \"Mit Pommes, bitte.\"",
            "timestamp": "2023-10-27T18:24:30Z"
          },
          {
            "direction": "incoming",
            "message": "Good choice. Just so you know, \"Pommes\" is pronounced like \"Pom-mess,\" not like the French way.",
            "timestamp": "2023-10-27T18:25:10Z"
          },
          {
            "direction": "outgoing",
            "message": "Thanks for the tip! \"Pom-mess.\" Okay, so I’ve ordered. Now the food arrives. Does the waiter say anything?",
            "timestamp": "2023-10-27T18:25:50Z"
          },
          {
            "direction": "incoming",
            "message": "He will put the food down and say \"Guten Appetit.\"",
            "timestamp": "2023-10-27T18:26:20Z"
          },
          {
            "direction": "outgoing",
            "message": "And I respond with \"Danke, gleichfalls\" if he is eating too? Wait, he's the waiter, he isn't eating.",
            "timestamp": "2023-10-27T18:26:55Z"
          },
          {
            "direction": "incoming",
            "message": "Haha, correct! Do not say \"gleichfalls\" (same to you) to the waiter! Just say \"Danke.\"",
            "timestamp": "2023-10-27T18:27:35Z"
          },
          {
            "direction": "outgoing",
            "message": "That would have been embarrassing. Okay, just \"Danke.\"",
            "timestamp": "2023-10-27T18:28:05Z"
          },
          {
            "direction": "incoming",
            "message": "While eating, if you need more napkins, you can ask: \"Könnte ich noch etwas Servietten haben, bitte?\"",
            "timestamp": "2023-10-27T18:28:50Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Könnte ich noch etwas Servietten haben?\" A bit of a tongue twister.",
            "timestamp": "2023-10-27T18:29:25Z"
          },
          {
            "direction": "incoming",
            "message": "Try \"Haben Sie noch eine Serviette?\" (Do you have another napkin?) It is easier.",
            "timestamp": "2023-10-27T18:30:00Z"
          },
          {
            "direction": "outgoing",
            "message": "Much easier. \"Haben Sie noch eine Serviette?\" Okay, I’m done eating. It was delicious.",
            "timestamp": "2023-10-27T18:30:35Z"
          },
          {
            "direction": "incoming",
            "message": "The waiter will come and ask: \"Hat es geschmeckt?\" (Did it taste good?)",
            "timestamp": "2023-10-27T18:31:10Z"
          },
          {
            "direction": "outgoing",
            "message": "I answer: \"Ja, es war sehr lecker.\" (Yes, it was very tasty).",
            "timestamp": "2023-10-27T18:31:45Z"
          },
          {
            "direction": "incoming",
            "message": "Perfect. \"Lecker\" is a very common word. Now, the awkward part for some tourists: The Bill.",
            "timestamp": "2023-10-27T18:32:30Z"
          },
          {
            "direction": "outgoing",
            "message": "Right. In the US we just wait for them to bring it. Do I have to ask in Germany?",
            "timestamp": "2023-10-27T18:33:10Z"
          },
          {
            "direction": "incoming",
            "message": "Yes, usually you have to ask. You can signal them and say: \"Die Rechnung, bitte.\" or \"Wir möchten gerne zahlen.\"",
            "timestamp": "2023-10-27T18:34:00Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Die Rechnung, bitte.\" (The bill, please). Do they usually take card or cash?",
            "timestamp": "2023-10-27T18:34:40Z"
          },
          {
            "direction": "incoming",
            "message": "In Berlin, cards are accepted often, but smaller places prefer cash (\"Bargeld\"). It is good to ask: \"Kann ich mit Karte zahlen?\"",
            "timestamp": "2023-10-27T18:35:30Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Kann ich mit Karte zahlen?\" Okay. And what about splitting the bill?",
            "timestamp": "2023-10-27T18:36:05Z"
          },
          {
            "direction": "incoming",
            "message": "Ah, good question. The waiter will almost always ask: \"Zusammen oder getrennt?\" (Together or separate?)",
            "timestamp": "2023-10-27T18:36:50Z"
          },
          {
            "direction": "outgoing",
            "message": "Oh, that’s what that means! I’ve heard that before in movies. So if I pay for myself, I say \"Getrennt, bitte.\"",
            "timestamp": "2023-10-27T18:37:35Z"
          },
          {
            "direction": "incoming",
            "message": "Exactly. And then you tell him what you ate, and he calculates it right at the table.",
            "timestamp": "2023-10-27T18:38:20Z"
          },
          {
            "direction": "outgoing",
            "message": "That’s convenient. Now, the big question: Tipping. What is the rule?",
            "timestamp": "2023-10-27T18:38:55Z"
          },
          {
            "direction": "incoming",
            "message": "It is not like the US where you must give 20%. In Germany, 5% to 10% is normal. We usually just round up.",
            "timestamp": "2023-10-27T18:39:40Z"
          },
          {
            "direction": "outgoing",
            "message": "Round up? How do I do that verbally?",
            "timestamp": "2023-10-27T18:40:15Z"
          },
          {
            "direction": "incoming",
            "message": "If the bill is 18.50 Euro, you hand him a 20 Euro note and say \"Stimmt so.\"",
            "timestamp": "2023-10-27T18:41:00Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Stimmt so.\" What does that mean literally?",
            "timestamp": "2023-10-27T18:41:30Z"
          },
          {
            "direction": "incoming",
            "message": "It means roughly \"It is correct like this\" or \"Keep the change.\"",
            "timestamp": "2023-10-27T18:42:05Z"
          },
          {
            "direction": "outgoing",
            "message": "Ah, okay. So if I pay by card, can I still tip?",
            "timestamp": "2023-10-27T18:42:40Z"
          },
          {
            "direction": "incoming",
            "message": "Yes, tell him the total amount you want to pay *before* he types it into the machine. If it is 45, say \"Machen Sie 50, bitte\" (Make it 50 please).",
            "timestamp": "2023-10-27T18:43:30Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Machen Sie 50, bitte.\" Got it. That is very helpful.",
            "timestamp": "2023-10-27T18:44:05Z"
          },
          {
            "direction": "incoming",
            "message": "I am glad! You seem ready for a German restaurant.",
            "timestamp": "2023-10-27T18:44:35Z"
          },
          {
            "direction": "outgoing",
            "message": "I feel much more confident. Can I ask one quick grammar question about the ordering?",
            "timestamp": "2023-10-27T18:45:10Z"
          },
          {
            "direction": "incoming",
            "message": "Of course, go ahead.",
            "timestamp": "2023-10-27T18:45:35Z"
          },
          {
            "direction": "outgoing",
            "message": "You said \"Ich hätte gerne.\" Is that the subjunctive? Like \"I would like\"?",
            "timestamp": "2023-10-27T18:46:15Z"
          },
          {
            "direction": "incoming",
            "message": "Yes, exactly. It comes from \"haben\" (to have). It is the Konjunktiv II form. It is much softer than saying \"Ich will\" (I want).",
            "timestamp": "2023-10-27T18:47:05Z"
          },
          {
            "direction": "outgoing",
            "message": "\"Ich will\" sounds rude?",
            "timestamp": "2023-10-27T18:47:35Z"
          },
          {
            "direction": "incoming",
            "message": "A little bit like a small child demanding something. \"Ich möchte\" or \"Ich hätte gerne\" is for adults and polite situations.",
            "timestamp": "2023-10-27T18:48:20Z"
          },
          {
            "direction": "outgoing",
            "message": "Noted. Never use \"Ich will\" at the waiter.",
            "timestamp": "2023-10-27T18:48:50Z"
          },
          {
            "direction": "incoming",
            "message": "Exactly. By the way, your English is perfect, so I didn't correct anything.",
            "timestamp": "2023-10-27T18:49:25Z"
          },
          {
            "direction": "outgoing",
            "message": "Thanks! Your English is really good too, actually. You said \"It is not like the US where you must give 20%.\" That was perfect grammar.",
            "timestamp": "2023-10-27T18:50:10Z"
          },
          {
            "direction": "incoming",
            "message": "Thank you! Sometimes I struggle with the prepositions. Did I say \"at the waiter\" earlier? I think I should have said \"to the waiter\"?",
            "timestamp": "2023-10-27T18:51:00Z"
          },
          {
            "direction": "outgoing",
            "message": "Yes, actually! \"Never use 'Ich will' *to* the waiter\" or \"*with* the waiter\" would be better. \"At the waiter\" sounds like you are throwing the words at him!",
            "timestamp": "2023-10-27T18:51:50Z"
          },
          {
            "direction": "incoming",
            "message": "Haha, okay. \"To the waiter.\" Thank you. Prepositions are hard in every language.",
            "timestamp": "2023-10-27T18:52:35Z"
          },
          {
            "direction": "outgoing",
            "message": "They really are. Well, this was a great session. I have a list of vocabulary to memorize now.",
            "timestamp": "2023-10-27T18:53:10Z"
          },
          {
            "direction": "incoming",
            "message": "Same here. I will remember \"split the bill\" instead of \"pay separate.\"",
            "timestamp": "2023-10-27T18:53:45Z"
          },
          {
            "direction": "outgoing",
            "message": "Yes, \"split the bill\" is very common. Thanks for your help, User B!",
            "timestamp": "2023-10-27T18:54:20Z"
          },
          {
            "direction": "incoming",
            "message": "You are welcome, User A. Have a great trip to Berlin!",
            "timestamp": "2023-10-27T18:54:50Z"
          },
          {
            "direction": "outgoing",
            "message": "Danke schön! Tschüss!",
            "timestamp": "2023-10-27T18:55:15Z"
          },
          {
            "direction": "incoming",
            "message": "Bitteschön! Tschüss!",
            "timestamp": "2023-10-27T18:55:40Z"
          }
        ]
    """.trimIndent()

    /**
     * Parses the userMessagesJson and returns a list of Message entities
     */
    fun getMessages(): List<Message> {
        return try {
            json.decodeFromString<List<Message>>(userMessagesJson)
        } catch (e: Exception) {
            println("Error parsing messages: ${e.message}")
            emptyList()
        }
    }
}

