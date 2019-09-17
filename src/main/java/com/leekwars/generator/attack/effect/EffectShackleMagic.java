package com.leekwars.generator.attack.effect;

import com.leekwars.generator.fight.Fight;
import com.leekwars.generator.fight.entity.Entity;

public class EffectShackleMagic extends Effect {

	@Override
	public void apply(Fight fight) {

		// Base shackle : base × (1 + magic / 100)
		value = (int) Math.round((value1 + jet * value2) * (1.0 + Math.max(0, caster.getMagic()) / 100.0) * power * criticalPower);
		if (value > 0) {
			stats.setStat(Entity.CHARAC_MAGIC, -value);
			target.updateBuffStats(Entity.CHARAC_MAGIC);
		}
	}
	
	public void reduce() {
		value /= 2;
		stats.setStat(Entity.CHARAC_MAGIC, -value);
		target.updateBuffStats(Entity.CHARAC_MAGIC);
	}
}
