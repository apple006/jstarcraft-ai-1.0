package com.jstarcraft.ai.neuralnetwork.layer;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.schedule.Schedule;
import com.jstarcraft.core.utility.RandomUtility;

/**
 * 默认掩码器
 * 
 * @author Birdy
 *
 */
public class DefaultMasker implements Masker {

	private Schedule schedule;

	public DefaultMasker(Schedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public void mask(MathMatrix matrix, int iteration, int epoch) {
		float current = schedule.valueAt(iteration, epoch);

		matrix.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			float value = scalar.getValue();
			scalar.setValue(RandomUtility.randomFloat(1F) < current ? 0F : value);
		});
	}

}
