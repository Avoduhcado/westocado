package com.avogine.westocado.utils.system;

public class Pair<T, J> {

	private T first;
	private J second;
	
	public Pair(T first, J second) {
		this.first = first;
		this.second = second;
	}
	
	public T getFirst() {
		return first;
	}
	
	public J getSecond() {
		return second;
	}
	
}
