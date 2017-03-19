package com.sky.reward.topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TopicService {

	private List<Topic> topics = new ArrayList<Topic>(Arrays.asList(
			new Topic("Spring", "Spring Framework", "Spring framework desc"),
			new Topic("Java", "Java Collections", "Collections desc"),
			new Topic("Linux", "Unix", "Red Hat Linux")
			));
	
	public List<Topic> getAllTopics() {
		return topics;
	}
	
	public Topic getTopic(String id) {
		return topics.stream().filter(e -> e.getId().equals(id))
					   .findFirst()
					   .get();
	}
	
	public void addTopic(Topic topic) {
		topics.add(topic);
	}

	public void updateTopic(String id, Topic topic) {
		
		for (int i = 0; i < topics.size(); i++) {
			Topic t = topics.get(i);
			if(t.getId().equals(id)){
				topics.set(i, topic);
				return;
			}
		}
	}
}
