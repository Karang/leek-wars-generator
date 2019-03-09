package com.leekwars.game.fight.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.leekwars.game.attack.chips.Chip;
import com.leekwars.game.attack.effect.Effect;
import com.leekwars.game.attack.effect.EffectPoison;
import com.leekwars.game.attack.weapons.Weapon;
import com.leekwars.game.fight.Fight;
import com.leekwars.game.fight.action.ActionRemoveEffect;
import com.leekwars.game.fight.action.ActionUpdateEffect;
import com.leekwars.game.leek.Leek;
import com.leekwars.game.leek.Register;
import com.leekwars.game.leek.Stats;
import com.leekwars.game.maps.Cell;
import com.leekwars.game.maps.Pathfinding;

public abstract class Entity {

	// Characteristics constants
	public final static int CHARAC_LIFE = 0;
	public final static int CHARAC_TP = 1;
	public final static int CHARAC_MP = 2;
	public final static int CHARAC_STRENGTH = 3;
	public final static int CHARAC_AGILITY = 4;
	public final static int CHARAC_FREQUENCY = 5;
	public final static int CHARAC_WISDOM = 6;
	public final static int CHARAC_ABSOLUTE_SHIELD = 9;
	public final static int CHARAC_RELATIVE_SHIELD = 10;
	public final static int CHARAC_RESISTANCE = 11;
	public final static int CHARAC_SCIENCE = 12;
	public final static int CHARAC_MAGIC = 13;
	public final static int CHARAC_DAMAGE_RETURN = 14;

	// Characteristics
	protected Cell cell;
	protected final Stats mBaseStats;
	protected final Stats mBuffStats;
	protected final String mName;
	protected final int mId;
	protected final int mFarmer;
	protected int mLevel;
	protected final int mSkin;
	protected final int mHat;
	protected String mFarmerName;
	protected String mFarmerCountry;
	protected String mTeamName = "";
	protected String mAIName;
	protected int mTeamId;
	protected int mAIId;
	protected int mTotalLife;

	// Current effects on the entity
	protected final ArrayList<Effect> effects = new ArrayList<Effect>();

	// Effects created by the entity
	private final ArrayList<Effect> launchedEffects = new ArrayList<Effect>();

	// Current cooldowns of the entity
	protected final Map<Integer, Integer> mCooldown = new TreeMap<Integer, Integer>();

	protected int team;

	protected Fight fight;
	protected EntityAI mEntityAI;

	private final TreeMap<Integer, Chip> mChips = new TreeMap<Integer, Chip>();

	private List<Weapon> mWeapons = null;
	private Weapon weapon = null;
	private boolean dead;

	private int usedTP;
	private int usedMP;
	private int life;

	private Register mRegister = null;

	private boolean mHasMoved = false;
	private boolean isDiabolic = false;
	private int fight_id;

	public Entity(Integer id, String name, int farmer, int level, int life, int turn_point, int move_point, int force, int agility, int frequency, int wisdom, int resistance, int science, int magic, int skin, int team_id, String team_name, int ai_id, String ai_name, String farmer_name, String farmer_country, int hat) {

		mId = id;
		mName = name;
		mLevel = level;
		mFarmer = farmer;
		mSkin = skin;
		mHat = hat;

		mBuffStats = new Stats();
		mBaseStats = new Stats();
		mBaseStats.setStat(CHARAC_LIFE, life);
		mBaseStats.setStat(CHARAC_TP, turn_point);
		mBaseStats.setStat(CHARAC_MP, move_point);
		mBaseStats.setStat(CHARAC_STRENGTH, force);
		mBaseStats.setStat(CHARAC_AGILITY, agility);
		mBaseStats.setStat(CHARAC_FREQUENCY, frequency);
		mBaseStats.setStat(CHARAC_WISDOM, wisdom);
		mBaseStats.setStat(CHARAC_RESISTANCE, resistance);
		mBaseStats.setStat(CHARAC_SCIENCE, science);
		mBaseStats.setStat(CHARAC_MAGIC, magic);

		mTotalLife = mBaseStats.getStat(CHARAC_LIFE);
		this.life = mTotalLife;

		mWeapons = new ArrayList<Weapon>();

		mTeamName = team_name;
		mTeamId = team_id;
		mFarmerName = farmer_name;
		mFarmerCountry = farmer_country;
		mAIName = ai_name;
		mAIId = ai_id;

		endTurn();
	}

	public Entity(Integer id, String name) {
		mId = id;
		mName = name;
		mLevel = 1;
		mFarmer = 0;
		mSkin = 0;
		mHat = -1;

		mBuffStats = new Stats();
		mBaseStats = new Stats();
		mBaseStats.setStat(CHARAC_LIFE, 0);
		mBaseStats.setStat(CHARAC_TP, 0);
		mBaseStats.setStat(CHARAC_MP, 0);
		mBaseStats.setStat(CHARAC_STRENGTH, 0);
		mBaseStats.setStat(CHARAC_AGILITY, 0);
		mBaseStats.setStat(CHARAC_FREQUENCY, 0);
		mBaseStats.setStat(CHARAC_WISDOM, 0);
		mBaseStats.setStat(CHARAC_RESISTANCE, 0);
		mBaseStats.setStat(CHARAC_SCIENCE, 0);
		mBaseStats.setStat(CHARAC_MAGIC, 0);

		mTotalLife = mBaseStats.getStat(CHARAC_LIFE);
		this.life = mTotalLife;

		mWeapons = new ArrayList<Weapon>();

		endTurn();
	}

	public Leek getLeek() {
		return null;
	}

	public int getOwnerId() {
		return -1;
	}

	public abstract int getType();

	public void setRegister(Register registre) {
		mRegister = registre;
	}

	public Register getRegister() {
		return mRegister;
	}

	public int getHat() {
		return mHat;
	}

	public int getAIId() {
		return mAIId;
	}

	public int getTeamId() {
		return mTeamId;
	}

	public String getTeamName() {
		return mTeamName;
	}

	public String getAIName() {
		return mAIName;
	}

	public String getFarmerName() {
		return mFarmerName;
	}

	public String getFarmerCountry() {
		if (mFarmerCountry == null) {
			return "?";
		}
		return mFarmerCountry;
	}
	public boolean isDiabolic() {
		return isDiabolic;
	}
	public void setDiabolic(boolean diabolic) {
		isDiabolic = diabolic;
	}
	public boolean hasMoved() {
		return mHasMoved;
	}
	public void setHasMoved(boolean moved) {
		mHasMoved = moved;
	}
	public void setAI(EntityAI ai) {
		this.mEntityAI = ai;
	}
	public void addWeapon(Weapon w) {
		mWeapons.add(w);
	}

	public Stats getBaseStats() {
		return mBaseStats;
	}

	public Cell getCell() {
		return cell;
	}
	public int getFId() {
		return fight_id;
	}
	public int getId() {
		return mId;
	}
	public EntityAI getLeekIA() {
		return mEntityAI;
	}
	public EntityAI getUsedLeekIA() {
		return mEntityAI;
	}
	public int getLevel() {
		return mLevel;
	}
	public int getLife() {
		return life;
	}

	public int getTotalLife() {
		return mTotalLife;
	}

	public void addTotalLife(int vitality) {
		mTotalLife += vitality;
	}

	public void setTotalLife(int vitality) {
		mTotalLife = vitality;
	}
	public String getName() {
		return mName;
	}
	public int getStat(int id) {
		return mBaseStats.getStat(id) + mBuffStats.getStat(id);
	}

	public int getStrength() {
		return getStat(Entity.CHARAC_STRENGTH);
	}

	public int getAgility() {
		return getStat(Entity.CHARAC_AGILITY);
	}

	public int getResistance() {
		return getStat(Entity.CHARAC_RESISTANCE);
	}

	public int getScience() {
		return getStat(Entity.CHARAC_SCIENCE);
	}

	public int getMagic() {
		return getStat(Entity.CHARAC_MAGIC);
	}

	public int getWisdom() {
		return getStat(Entity.CHARAC_WISDOM);
	}

	public int getRelativeShield() {
		return getStat(Entity.CHARAC_RELATIVE_SHIELD);
	}

	public int getAbsoluteShield() {
		return getStat(Entity.CHARAC_ABSOLUTE_SHIELD);
	}

	public int getDamageReturn() {
		return getStat(Entity.CHARAC_DAMAGE_RETURN);
	}

	public int getFrequency() {
		return getStat(Entity.CHARAC_FREQUENCY);
	}

	public int getTotalTP() {
		return getStat(Entity.CHARAC_TP);
	}

	public int getTotalMP() {
		return getStat(Entity.CHARAC_MP);
	}

	public int getMP() {
		return getTotalMP() - usedMP;
	}

	public int getTP() {
		return getTotalTP() - usedTP;
	}

	public int getTeam() {
		return team;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public boolean hasWeapon(int id_tmp) {
		for (Weapon w : mWeapons) {
			if (w.getId() == id_tmp)
				return true;
		}
		return false;
	}

	public List<Weapon> getWeapons() {
		return mWeapons;
	}

	public boolean isDead() {
		return dead;
	}

	public void removeLife(int pv, int erosion, Entity attacker, boolean direct_attack) {
		if (pv > life) {
			pv = life;
		}
		life -= pv;
		fight.statistics.addDammages(pv);

		// Add erosion
		mTotalLife -= erosion;
		if (mTotalLife < 1) mTotalLife = 1;

		// Add damage statistic only if it's an enemy
		if (this.team != attacker.team) {
			fight.getTrophyManager().addDamage(attacker, pv);
		}

		// Sniper
		if (this.team != attacker.team && direct_attack
				&& attacker.getCell() != null && Pathfinding.getCaseDistance(this.cell, attacker.getCell()) > 10) {
			fight.getTrophyManager().sniper(attacker);
		}

		if (life <= 0) {
			fight.onPlayerDie(this, attacker);
			die();
		}
	}

	public void addLife(int pv) {
		if (pv > getTotalLife() - life)
			pv = getTotalLife() - life;
		fight.statistics.addHeal(pv);
		life += pv;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void setFight(Fight fight, int fight_id) {
		this.fight = fight;
		if (mEntityAI != null)
			this.mEntityAI.setFight(this.fight);
		this.fight_id = fight_id;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	// At the start of his turn, decrease duration of his launched effects
	// and apply effects that affects the entity at the beginning of his turn
	// (poisons, ...)
	public void startTurn() {

		for (Effect effect : this.effects) {
			effect.applyStartTurn(fight);
			if (dead) {
				return;
			}
		}

		for (int e = 0; e < launchedEffects.size(); ++e) {

			Effect effect = launchedEffects.get(e);

			effect.setTurns(effect.getTurns() - 1); // Decrease duration

			if (effect.getTurns() <= 0) { // Effect done

				effect.getTarget().removeEffect(effect);
				launchedEffects.remove(e);
				e--;
			}
		}
	}

	// Restore TP and MP at the end of turn
	public void endTurn() {

		usedMP = 0;
		usedTP = 0;
	}

	// When entity dies
	public void die() {

		// Remove launched effects
		while (launchedEffects.size() > 0) {

			Effect effect = launchedEffects.get(0);

			effect.getTarget().removeEffect(effect);
			launchedEffects.remove(0);
		}

		// Kill summons
		List<Entity> entities = new ArrayList<Entity>(fight.getTeamEntities(getTeam()));
		for (Entity e : entities) {
			if (e.getOwnerId() == getFId()) {
				fight.onPlayerDie(e, null);
				e.die();
			}
		}
	}

	public void updateBuffStats() {
		mBuffStats.clear();
		for (Effect effect : effects) {
			if (effect.getStats() != null)
				mBuffStats.addStats(effect.getStats());
		}
	}

	public void updateBuffStats(int id) {
		mBuffStats.setStat(id, 0);
		for (Effect effect : effects) {
			if (effect.getStats() != null)
				mBuffStats.addStat(id, effect.getStats().getStat(id));
		}
	}

	public void addEffect(Effect effect) {
		effects.add(effect);
	}

	/*
	 * Remove an active effect of the entity. Also add a fight log
	 */
	public void removeEffect(Effect effect) {

		fight.log(new ActionRemoveEffect(effect.getLogID()));
		effects.remove(effect);

		updateBuffStats();
	}

	public void addLaunchedEffect(Effect effect) {
		launchedEffects.add(effect);
	}

	public void removeLaunchedEffect(Effect effect) {
		launchedEffects.remove(effect);
	}
	
	public void updateEffect(Effect effect) {
		fight.log(new ActionUpdateEffect(effect.getLogID(), effect.value));
	}

	public void clearEffects() {

		for (int i = 0; i < effects.size(); ++i) {

			Effect effect = effects.get(i);

			effect.getCaster().removeLaunchedEffect(effect);
			removeEffect(effect);
			i--;
		}

		effects.clear();
	}
	
	public void reduceEffects() {
		for (int i = 0; i < effects.size(); ++i) {
			effects.get(i).reduce();
			updateEffect(effects.get(i));
		}
		updateBuffStats();
	}

	public void clearPoisons() {

		for (int i = 0; i < effects.size(); ++i) {

			Effect effect = effects.get(i);

			if (effect instanceof EffectPoison) {
				effect.getCaster().removeLaunchedEffect(effect);
				removeEffect(effect);
				i--;
			}
		}
	}

	public void applyCoolDown() {
		Map<Integer, Integer> cooldown = new TreeMap<Integer, Integer>();
		cooldown.putAll(mCooldown);
		for (Entry<Integer, Integer> chip : cooldown.entrySet()) {
			if (chip.getValue() <= 1)
				mCooldown.remove(chip.getKey());
			else
				mCooldown.put(chip.getKey(), chip.getValue() - 1);
		}
	}

	public void addChip(Chip chip) {
		if (chip != null)
			mChips.put(chip.getId(), chip);
	}

	// Chip has just been used, we must store the cooldown (entity cooldown)
	public void addCooldown(Chip chip, int cooldown) {

		mCooldown.put(chip.getId(), cooldown == -1 ? Fight.MAX_TURNS + 2 : cooldown);
	}

	// Entity has cooldown for this chip?
	public boolean hasCooldown(int chipID) {
		return mCooldown.containsKey(chipID);
	}

	// Get current cooldown for a chip
	public int getCooldown(int chipID) {
		if (!hasCooldown(chipID)) {
			return 0;
		}
		return mCooldown.get(chipID);
	}
	public int getFarmer() {
		return mFarmer;
	}

	public Chip getChip(int id) {
		return mChips.get(id);
	}

	public List<Chip> getChips() {
		return new ArrayList<Chip>(mChips.values());
	}

	public boolean hasValidAI(int fight_type) {
		if (mEntityAI == null)
			return false;
		return mEntityAI.isValid();
	}

	public int getSkin() {
		return mSkin;
	}

	public List<Effect> getEffects() {
		return effects;
	}

	public List<Effect> getLaunchedEffects() {
		return launchedEffects;
	}

	public void setLevel(int level) {
		mLevel = level;
	}

	public void resurrect(Entity entity) {
		clearEffects();
		mTotalLife = Math.max(10, mTotalLife / 2);
		life = mTotalLife / 2;
		dead = false;
		endTurn();
	}

	public int getAppearance() {
		return Entity.getAppearence(getLevel());
	}

	public static int getAppearence(int level) {
		if (level <= 9)
			return 1;
		else if (level <= 19)
			return 2;
		else if (level <= 49)
			return 3;
		else if (level <= 79)
			return 4;
		else if (level <= 99)
			return 5;
		else if (level <= 149)
			return 6;
		else if (level <= 199)
			return 7;
		else if (level <= 249)
			return 8;
		else if (level <= 299)
			return 9;
		else if (level <= 300)
			return 10;
		else
			return 11;
	}

	public void useTP(int tp) {
		fight.statistics.addTPUsed(tp);
		usedTP += tp;
	}

	public void useMP(int mp) {
		fight.statistics.addMPUsed(mp);
		usedMP += mp;
	}

	@Override
	public String toString() {
		return mName;
	}

	public boolean isAlive() {
		return !isDead();
	}

	public EntityAI getAI() {
		return mEntityAI;
	}
}
