package com.fitness.aiservice.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Answer;
import com.fitness.aiservice.model.CustomRecommendation;
import com.fitness.aiservice.model.Recommendation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Slf4j
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){

        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getRecommendations(prompt);
        
        return processAIResponse(activity, aiResponse);
        
    }

    public Answer generateAnswer(String question){

        String prompt = generateGeneralPrompt(question);
        String aiResponse = geminiService.getRecommendations(prompt);
        return processAnswerResponse(aiResponse, question);
    }

    private Answer processAnswerResponse(String aiResponse, String question) {


     ObjectMapper objectMapper = new ObjectMapper();

     try{
     String jsonContent = aiResponse
                .replaceAll("```json\\n", "")
                .replaceAll("```", "")
                .trim();

                return objectMapper.readValue(jsonContent, Answer.class);
    } catch(Exception e){
        log.error("Failed to parse answer JSON: {}", aiResponse, e);
        return new Answer(question  , "No answer generated");
    }
}

    public CustomRecommendation generateCustomRecommendation(String keycloakId, List<Recommendation> recommendations) {
       String prompt = createPromptForCustomRecommendation(recommendations);
       if (recommendations.isEmpty()) {
            return defaultCustomRecommendation();
        }
            String aiResponse = geminiService.getRecommendations(prompt);
            return processCustomRecommendationResponse(aiResponse);
    }


    private CustomRecommendation defaultCustomRecommendation() {
        CustomRecommendation.Safety safety = new CustomRecommendation.Safety(
            Arrays.asList(
                "Always warm up before exercise",
                "Stay Hydrated",
                "Always listen to your body"
            ),
            Arrays.asList(
                "Avoid overexertion",
                "Don't skip rest days"
            )
        );

        return new CustomRecommendation(
            "No activities available",
            "No heart rate guidance available",
            "No water intake recommendation available",
            "No distance target available",
            "Eat a balanced meal before your next workout",
            "Eat a protein-rich meal after your next workout",
            "No specific routine recommended due to lack of data",
            safety
        );
    }


    private Recommendation processAIResponse(Activity activity, String aiResponse) {

        try{

            ObjectMapper objectMapper = new ObjectMapper();

            String jsonContent = aiResponse
                                 .replaceAll("```json\\n", "")
                                 .replaceAll("\\n```", "")
                                 .trim();

            JsonNode analysisJson = objectMapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

             Recommendation recommendation = Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getType().toString())
                .recommendation(fullAnalysis.toString().trim())
                .improvements(improvements)
                .suggestions(suggestions)
                .safety(safety)
                .createdAt(LocalDateTime.now())
                .build();

                log.info("Generated recommendation for activity: {}", recommendation);

                return recommendation;

        }
        catch(Exception e){
            e.printStackTrace();
            return createdDefaultRecommendation(activity);
        }  
    }

    private Recommendation createdDefaultRecommendation(Activity activity) {
     return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getType().toString())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routein"))
                .suggestions(Collections.singletonList("Consider consulting a fitness consultant"))
                .safety(Arrays.asList(
                    "Always warm up before exercise",
                    "Stay Hydrated",
                    "Always listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
      
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()) {
            improvementsNode.forEach((improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            }));
        }

        return improvements.isEmpty() ?
        Collections.singletonList("No specific improvements provided.") : 
        improvements;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach((suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));
            }));
        }

        return suggestions.isEmpty() ?
        Collections.singletonList("No specific suggestions provided.") : 
        suggestions;
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if (safetyNode.isArray()) {
            safetyNode.forEach((item -> {
                safety.add(item.asText());
            }));
        }

        return safety.isEmpty() ?
        Collections.singletonList("Follow general safety guidelines") : 
        safety;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (!analysisNode.path(key).isMissingNode()) {
            fullAnalysis.append(prefix)
            .append(analysisNode.path(key)
            .asText())
            .append("\n\n");
        }
    }

    private CustomRecommendation processCustomRecommendationResponse(String aiResponse) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonContent = aiResponse
                .replaceAll("```json\\n", "")
                .replaceAll("```", "")
                .trim();

        return objectMapper.readValue(jsonContent, CustomRecommendation.class);

    } catch (Exception e) {
        log.error("Failed to parse custom recommendation JSON: {}", aiResponse, e);
        return null;
    }
}

    private String createPromptForActivity(Activity activity) {

         String metricsText = "None";
    try {
        metricsText = new ObjectMapper().writeValueAsString(activity.getAdditionalMetrics());
    } catch (Exception e) {
        metricsText = String.valueOf(activity.getAdditionalMetrics());
    }
        
        return String.format("""
             Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                metricsText
        );
    }

        private String generateGeneralPrompt(String question) {
        return String.format("""

                    You are a helpful assistant. Before answering, silently rewrite the user's
                    question into a clearer, more specific version if it is vague or ambiguous,
                    then answer that improved question directly.
                    Respond strictly with only this JSON format below:
                    {
                    "question": "%s",
                    "answer": "{Your generated answer here, based on the improved question.}"
                    }

                    Strictly keep in mind that, if the user asks any ambigious or irrelevent question other than 
                    fitness, health or exercise related, you should respond with A STRICT WARNING generated by you based on the questions ambiguity
                     in the answer 
                    field of the JSON response.
                    """, 
                    question);
        
    }

    private String createPromptForCustomRecommendation(List<Recommendation> recommendations) {
        return String.format(
            """
                You are a fitness coach reviewing all of a user's tracked activities till this day.
    Analyze the activities below and produce ONE consolidated daily recommendation.

    Pick the activity that had the most impact (duration, intensity, or effort) as the
    primary "type", and justify why this routine makes sense for today specifically.

    Respond ONLY in this EXACT JSON format, with no markdown fences or extra text:
    {
      "type": "PRIMARY_ACTIVITY_TYPE",
      "heartRate": "Recommended heart rate guidance based on the user's recent activities",
      "waterIntake": "Recommended water intake for today",
      "distance": "Recommended distance target for today, if applicable",
      "eatBefore": "What to eat before the next workout",
      "eatAfter": "What to eat after the next workout",
      "whyThisRoutine": "Why this specific routine/activity is the right choice for today, based on the data",
      "safety": {
        "dos": ["Do this...", "Do that..."],
        "donts": ["Don't do this if...", "Avoid that when..."]
      }
    }

    Rules:
    - Keep each value concise (1-2 sentences) and practical.
    - Ground every recommendation ONLY in the data provided below. Do not invent numbers
      you can't reasonably infer from the data.
    - "dos" and "donts" should each have 2-4 points, phrased as short actionable statements.
    - If "distance" isn't applicable to the activity type (e.g. strength training), return
      an empty string for it rather than fabricating a value.

     Activities till now (%d total):
    %s
        """,
    
    recommendations.size(), 
    buildActivitiesBlock(recommendations)
);
    }

    private String buildActivitiesBlock(List<Recommendation> recommendations) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < recommendations.size(); i++) {
        Recommendation r = recommendations.get(i);
        sb.append(String.format(
            """
            Activity %d:
            Type: %s
            Analysis: %s
            Improvements: %s
            Suggestions: %s
            Safety: %s

            """,
            i + 1,
            r.getType(),
            r.getRecommendation(),
            String.join(" | ", r.getImprovements()),
            String.join(" | ", r.getSuggestions()),
            String.join(" | ", r.getSafety())
        ));
    }
    return sb.toString();
}

}
