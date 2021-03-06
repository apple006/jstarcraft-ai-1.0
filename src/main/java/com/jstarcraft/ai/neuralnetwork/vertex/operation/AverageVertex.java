package com.jstarcraft.ai.neuralnetwork.vertex.operation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Euclidean节点
 * 
 * <pre></pre>
 * 
 * @author Birdy
 *
 */
public class AverageVertex extends PlusVertex {

	protected AverageVertex() {
	}

	public AverageVertex(String name, MatrixFactory factory) {
		super(name, factory);
	}

	@Override
	public void doForward() {
		super.doForward();
		MathMatrix outputData = outputKeyValue.getKey();
		float scale = 1F / inputKeyValues.length;
		outputData.scaleValues(scale);
	}

	@Override
	public void doBackward() {
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			float error = scalar.getValue() / inputKeyValues.length;
			for (KeyValue<MathMatrix, MathMatrix> keyValue : inputKeyValues) {
				MathMatrix outerError = keyValue.getValue();
				if (outerError != null) {
					// TODO 使用累计的方式计算
					// TODO 需要锁机制,否则并发计算会导致Bug
					outerError.shiftValue(scalar.getRow(), scalar.getColumn(), error);
				}
			}
		});
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		} else {
			AverageVertex that = (AverageVertex) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.vertexName, that.vertexName);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vertexName);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "AverageVertex(name=" + vertexName + ")";
	}

}
