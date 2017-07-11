package com.processing.inherit;

import java.util.ArrayList;

/**
 * �߶ι�����
 * */
public class Line {
	private ArrayList<Part> lines = new ArrayList<>();
	private int total = 0;
	
	/**
	 * ���һ������
	 * */
	public boolean add(int length, Object attachment){
		if(length <= 0){
			return false;
		}
		Part line = new Part(length, attachment);
		total += length;
		lines.add(line);
		return true;
	}
	
	/**
	 * ����һ��ֵ��ɾ���ڴ˷�Χ��Part
	 *  @param value : value >= 0 && value < getTotal()
	 * */
	public boolean remove(int value){
		Part part = getPart(value);
		if(part != null){
			lines.remove(part);
			total -= part.getLength();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ����һ��ֵ���ض����ڴ˷�Χ��Part�ĳ���
	 * @param value : value >= 0 && value < getTotal()
	 * */
	public boolean resetLength(int value, int changedValue){
		Part part = getPart(value);
		if(part != null){
			int afterLength = changedValue + part.getLength();
			if(afterLength > 0){
				part.setLength(afterLength);
				total += changedValue;
			} else {
				remove(value);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ��ݴ���ĸ��������ص�1��ƥ��˸����Ķ���Χ��һ�����ֵ��������ƥ��ø����Ķ��󽫷���-1
	 * */
	public int getValue(Object attachment){
		return getValue(attachment, 1);
	}
	
	/**
	 * ��ݴ���ĸ��������ص�num��ƥ��˸����Ķ���Χ��һ�����ֵ��������ƥ��ø����Ķ��󽫷���-1
	 * */
	public int getValue(Object attachment, int num){
		int result = 0;
		int count = 0;
		Part resultPart = null;
		for(Part part : lines){
			if(attachment == null && part.getAttachment() == null){
				++ count;
				if(count == num){
					resultPart = part;
				}
				break;
			} else if(attachment != null && part.getAttachment() != null && part.getAttachment().equals(attachment)){
				++ count;
				if(count == num){
					resultPart = part;
				}
				break;
			} else {
				result += part.getLength();
			}
		}
		if(resultPart == null){
			result = -1;
		} else {
			result += Math.random() * resultPart.getLength();
		}
		return result;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int size(){
		return lines.size();
	}

	/**
	 * ����һ��ֵ����ȡ�ڴ˷�Χ�ĸ���
	 * @param value : value >= 0 && value < getTotal()
	 * */
	public Object get(int value){
		Part part = getPart(value);
		return part == null ? null : part.getAttachment();
	}
	/**
	 * ����һ��ֵ����ȡ�ڴ˷�Χ��endֵ
	 * @param value : value >= 0 && value < getTotal()
	 * */
	public int getEnd(int value){
		int end = 0;
		Part part = getPart(value);
		if(part != null){
			for(Part p : lines){
				end += p.getLength();
				if(p.equals(part)){
					break;
				}
			}
			end -= 1;
		}
		return end;
	}
	/**
	 * ����һ��ֵ����ȡ�ڴ˷�Χ��beginֵ
	 * @param value : value >= 0 && value < getTotal()
	 * */
	public int getBegin(int value){
		int begin = 0;
		Part part = getPart(value);
		if(part != null){
			for(Part p : lines){
				if(p.equals(part)){
					break;
				}
				begin += p.getLength();
			}
		}
		return begin;
	}
	
	/**
	 * ����һ��ֵ����ȡ�ڴ˷�Χ��Part������ֵ
	 * */
	public int getIndex(int value){
		Part part = getPart(value);
		if(part != null){
			for(int i = 0; i < lines.size(); ++ i){
				if(lines.get(i).equals(part)){
					return i;
				}
			}
		}
		return -1;
	}
	/**
	 * ����һ��ֵ����ȡ�ڴ˷�Χ��Part
	 * */
	private Part getPart(int value){
		++ value;
		if(value > 0 && value <= total && lines.size() > 0){
			for(Part line : lines){
				value -= line.getLength();
				if(value <= 0){
					return line;
				}
			}
		}
		return null;
	}
	
	public Object getRandom(){
		int random = (int) (Math.random() * total);
		return get(random);
	}
	/**
	 * ���֣� ��ݽṹ 
	 * @author jiawei
	 * */
	private class Part {
		private int length;
		private Object attachment;//����
		public Part(int length, Object attachment) {
			this.length = length;
			this.attachment = attachment;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public Object getAttachment() {
			return attachment;
		}
		@Override
		public String toString() {
			return "Part [length=" + length + ", attachment=" + attachment
					+ "]";
		}
	}
}
