/**
 * 
 */
package com.taobao.top.xbox.baseline;

/**
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-8
 *
 */
public class ConfidenceArea {
	private double left;
	private double right;
	private double value;
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	
	public double getLeft() {
		return left;
	}
	public void setLeft(double left) {
		this.left = left;
	}
	public double getRight() {
		return right;
	}
	public void setRight(double right) {
		this.right = right;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new StringBuilder().append("ConfidenceArea: ")
				.append("value : ").append(value).append(", ")
				.append("left : ").append(left).append(", ")
				.append("right: ").append(right).toString();
	}
	
	
}
