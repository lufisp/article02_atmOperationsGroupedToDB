package model;

import java.util.List;

public enum SingletonVariablesShare {
	INSTANCE;
	private List<DataModel> lastMessagesMicroBatch;

	public List<DataModel> getLastMessagesMicroBatch() {
		return lastMessagesMicroBatch;
	}

	public SingletonVariablesShare setLastMessagesMicroBatch(List<DataModel> lastMessagesMicroBatch) {
		this.lastMessagesMicroBatch = lastMessagesMicroBatch;
		return this;
	}
	
	
}
