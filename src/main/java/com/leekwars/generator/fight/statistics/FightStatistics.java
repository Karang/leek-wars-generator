package com.leekwars.generator.fight.statistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.leekwars.generator.attack.Attack;
import com.leekwars.generator.attack.chips.Chip;
import com.leekwars.generator.attack.effect.Effect;
import com.leekwars.generator.attack.weapons.Weapon;
import com.leekwars.generator.fight.entity.Entity;
import com.leekwars.generator.maps.Cell;

public class FightStatistics {

	public static int KILLS = 12;
	public static int BULLETS = 13;
	public static int USED_CHIPS = 14;
	public static int SUMMONS = 17;
	public static int DAMAGE = 30;
	public static int HEAL = 31;
	public static int DISTANCE = 32;
	public static int STACK_OVERFLOWS = 34;
	public static int ERRORS = 33;
	public static int RESURRECTS = 47;
	public static int DAMAGE_POISON = 48;
	public static int DAMAGE_RETURN = 49;
	public static int CRITICAL_HITS = 50;
	public static int TP_USED = 51;
	public static int MP_USED = 52;
	public static int OPERATIONS = 53;
	public static int SAYS = 54;
	public static int SAYS_LENGTH = 55;

	/*
	 * Global fight statistics
	 */
	private int sKills = 0;
	private int sBullets = 0;
	private int sUsedChips = 0;
	private int sSummons = 0;
	private long sDammages = 0;
	private long sHeal = 0;
	private long sDistance = 0;
	private int sStackOverflow = 0;
	private int sErrors = 0;
	private int sResurrects = 0;
	private long sDamagePoison = 0;
	private long sDamageReturn = 0;
	private int sCriticalHits = 0;
	private int sTPUsed = 0;
	private int sMPUsed = 0;
	private long sOperations = 0;
	private int sSays = 0;
	private long sSaysLength = 0;

	/*
	 * Statistics per farmer
	 */
	private final Map<Integer, FarmerStatistics> farmers;

	public FightStatistics() {
		farmers = new TreeMap<Integer, FarmerStatistics>();
	}

	public void initializeEntities(Collection<Entity> entities) {
		for (Entity entity : entities) {
			if (!this.farmers.containsKey(entity.getFarmer())) {
				this.farmers.put(entity.getFarmer(), new FarmerStatistics());
			}
		}
	}

	public void addStackOverflow(Entity entity) {
		sStackOverflow++;
		this.farmers.get(entity.getFarmer()).stackOverflows++;
	}

	public int getStackOverflow() {
		return sStackOverflow;
	}

	public void addDistance(int n) {
		sDistance += n;
	}

	public long getDistance() {
		return sDistance;
	}

	public void addHeal(int n) {
		sHeal += n;
	}

	public long getHeal() {
		return sHeal;
	}

	public long getDamage() {
		return sDammages;
	}

	public void summon(Entity caster, Entity summon) {
		sSummons += 1;
		farmers.get(caster.getFarmer()).summons++;
	}

	public int getSummons() {
		return sSummons;
	}

	public int getUsedChips() {
		return sUsedChips;
	}

	public int getBullets() {
		return sBullets;
	}

	public void addKills(int n) {
		sKills += n;
	}

	public int getKills() {
		return sKills;
	}

	public void addErrors(int errors) {
		sErrors += errors;
	}

	public int getErrors() {
		return sErrors;
	}

	public long getDamagePoison() {
		return sDamagePoison;
	}

	public void addDamagePoison(long damagePoison) {
		this.sDamagePoison += damagePoison;
	}

	public int getResurrects() {
		return sResurrects;
	}

	public void addResurrects(int resurrects) {
		this.sResurrects += resurrects;
	}

	public long getDamageReturn() {
		return sDamageReturn;
	}

	public void addDamageReturn(long damageReturn) {
		this.sDamageReturn += damageReturn;
	}

	public int getTPUsed() {
		return sTPUsed;
	}

	public void addTPUsed(int TPUsed) {
		this.sTPUsed += TPUsed;
	}

	public int getCriticalHits() {
		return sCriticalHits;
	}

	public void addCriticalHits(int criticalHits) {
		this.sCriticalHits += criticalHits;
	}

	public int getMPUsed() {
		return sMPUsed;
	}

	public void addMPUsed(int MPUsed) {
		this.sMPUsed += MPUsed;
	}

	public long getOperations() {
		return sOperations;
	}

	public void addOperations(long operations) {
		this.sOperations += operations;
	}

	public int getSays() {
		return sSays;
	}

	public void addSays(int says) {
		this.sSays += says;
	}

	public long getSaysLength() {
		return sSaysLength;
	}

	public void addSaysLength(long saysLength) {
		this.sSaysLength += saysLength;
	}

	public void stashed(Entity caster) {
		this.farmers.get(caster.getFarmer()).stashed++;
	}

	public void roxxor(Entity caster) {
		this.farmers.get(caster.getFarmer()).roxxor++;
	}

	public void checkCharacteristics(Entity entity) {
		FarmerStatistics stats = this.farmers.get(entity.getFarmer());
		if (entity.getLife() > stats.maxEntityLife) {
			stats.maxEntityLife = entity.getLife();
		}
		if (entity.getTP() > stats.maxEntityTP) {
			stats.maxEntityTP = entity.getTP();
		}
		if (entity.getLife() > stats.maxEntityMP) {
			stats.maxEntityMP = entity.getMP();
		}
	}

	public void entityMove(Entity entity, List<Cell> path) {
		sDistance += path.size();
		FarmerStatistics stats = this.farmers.get(entity.getFarmer());
		stats.walkedDistance += path.size();
		// Save walked cells
		for (Cell c : path) {
			stats.walkedCells.set(entity.getId(), c.getId());
		}
	}

	public void chipUsed(Entity caster, Chip chip, List<Entity> targets) {
		sUsedChips++;
		FarmerStatistics stats = this.farmers.get(caster.getFarmer());
		stats.usedChips++;
		stats.chipsUsed.add(caster.getId(), chip.getId());
		attackUsed(caster, targets, chip.getAttack());
	}

	public void weaponUsed(Entity launcher, Weapon weapon, List<Entity> targets) {
		sBullets++;
		FarmerStatistics stats = this.farmers.get(launcher.getFarmer());
		stats.weaponShot++;
		stats.weaponsUsed.add(launcher.getId(), weapon.getId());
		attackUsed(launcher, targets, weapon.getAttack());
	}

	private void attackUsed(Entity caster, List<Entity> targets, Attack attack) {

		FarmerStatistics stats = this.farmers.get(caster.getFarmer());

		// Le mec s'est suicidé avec son attaque ?
		if (caster.isDead()) {
			stats.suicides++;
		}

		int hurt_enemies = 0;
		int healed_enemies = 0;
		int killed_allies = 0;
		int killed_enemies = 0;

		for (Entity target : targets) {
			if (target.getTeam() != caster.getTeam()) { // Ennemi
				if (target.isDead()) {
					killed_enemies++;
				}
				if (attack.isDamageAttack(Effect.TARGET_ENEMIES)) {
					hurt_enemies++;
				}
				if (attack.isHealAttack(Effect.TARGET_ENEMIES) && !attack.isDamageAttack(Effect.TARGET_ENEMIES)) {
					healed_enemies++;
				}
			} else if (target.getId() != caster.getId()) { // Allié
				if (target.isDead()) {
					killed_allies++;
				}
			}
		}
		// Cibles tuées
		stats.kills += killed_allies + killed_enemies;
		// Kamikaze ?
		if (caster.isDead() && killed_enemies > 0) {
			stats.kamikaze++;
		}
		// Tuer un allié
		if (killed_allies > 0) {
			stats.killedAllies++;
		}
		// Soigner un ennemi
		if (healed_enemies > 0) {
			stats.healedEnemies++;
		}
		// Toucher plusieurs ennemis
		if (hurt_enemies > stats.maxHurtEnemies) {
			stats.maxHurtEnemies = hurt_enemies;
		}
		// Tuer plusieurs ennemis
		if (killed_enemies > stats.maxKilledEnemies) {
			stats.maxKilledEnemies = killed_enemies;
		}
	}

	public void endFight(Collection<Entity> entities) {
		// Save end cells of each entity
		for (Entity entity : entities) {
			this.farmers.get(entity.getFarmer()).endCells.set(entity.getId(), entity.getCell().getId());
		}
	}

	public void addDamage(Entity attacker, int amount, boolean isEnemy) {
		sDammages += amount;
		if (isEnemy) {
			this.farmers.get(attacker.getFarmer()).damage += amount;
		}
	}

	public void sniper(Entity attacker) {
		this.farmers.get(attacker.getFarmer()).snipers++;
	}

	public void lama(Entity entity) {
		this.farmers.get(entity.getFarmer()).lamas++;
	}

	public void tooMuchOperations(Entity entity) {
		this.farmers.get(entity.getFarmer()).tooMuchOperations++;
	}
}
