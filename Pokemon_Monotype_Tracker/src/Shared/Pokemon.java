package Shared;

import Battle.Moves.Move;
import Battle.StatusCondition;

public class Pokemon {
    private int pokedexID;
    private String name;
    private PokemonType primaryType;
    private PokemonType secondaryType;
    private int[] baseStats;
    private int level;
    private int maxHP;
    private int currentHP;
    private int attack;
    private int defense;
    private int special;
    private int speed;
    private int accuracy;
    private int evasion;
    private boolean defeated;
    private Move[] knownMoves;
    private StatusCondition appliedStatusCondition;

    public void setAppliedStatusCondition(StatusCondition appliedStatusCondition) {
        this.appliedStatusCondition = appliedStatusCondition;
    }

    public int[] getBaseStats() {
        return baseStats;
    }

    public void setBaseStats(int[] baseStats) {
        this.baseStats = baseStats;
    }

    public StatusCondition getAppliedStatusCondition() {
        return appliedStatusCondition;
    }

    public void setKnownMoves(Move[] knownMoves) {
        this.knownMoves = knownMoves;
    }

    public Move[] getKnownMoves() {
        return knownMoves;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    public int getPokedexID() {
        return pokedexID;
    }

    public String getName() {
        return name;
    }

    public PokemonType getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(PokemonType primaryType) {
        this.primaryType = primaryType;
    }

    public PokemonType getSecondaryType() {
        return secondaryType;
    }

    public void setSecondaryType(PokemonType secondaryType) {
        this.secondaryType = secondaryType;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpecial() {
        return special;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getEvasion() {
        return evasion;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public Pokemon() {}

    public Pokemon(int pokedexID, String name, PokemonType primaryType, PokemonType secondaryType, int level, int[] baseStats, int[] ivs) {
        this.pokedexID = pokedexID;
        this.name = name;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.level = level;
        this.baseStats = baseStats;
        int[] stats = calculateStats(baseStats, ivs, level);
        this.maxHP = stats[0];
        this.currentHP = maxHP;
        this.attack =  stats[1];
        this.defense = stats[2];
        this.special = stats[3];
        this.speed = stats[4];
        this.accuracy = 1;
        this.evasion = 1;
        this.defeated = false;
    }

    public String showBaseStats() {
        return "HP: " + maxHP + "\nAttack: " + attack  + "\nDefense: " + defense + "\nSpecial: " + special + "\nSpeed: " + speed;
    }

    public String showTypes() {
        String out = "";
        String[] typeNames = {
                "Normal",
                "Fighting",
                "Flying",
                "Poison",
                "Ground",
                "Bug",
                "Ghost",
                "Fire",
                "Water",
                "Grass",
                "Electric",
                "Psychic",
                "Ice",
                "Dragon"
        };
        // Decrement the type by 1 for the index here since we don't include the 'None' Type
        out += typeNames[primaryType.ordinal() - 1];
        if (secondaryType != PokemonType.None) {
            out += ", " +  typeNames[secondaryType.ordinal() - 1];
        }
        return out;
    }

    public String toString() {
        return "Level " + level + " " + this.name + "\n" + showBaseStats() + "\nTypes: [" +  showTypes() + "]";
    }

    int[] calculateStats(int[] baseStats, int[] ivs, int level) {
        final int HP_IDX = 0;
        int[] results = new int[baseStats.length];
        for (int i = 0; i < baseStats.length; i++) {
            // Each stat would use a value x
            // x = Math.floor((Math.sqrt(SE - 1) + 1) / 4)
            // Since we assume that SE == 0, x can be omitted
            int y = (i == HP_IDX) ? level + 10 : 5;
            int stat = (((baseStats[i] + ivs[i]) * 2) * level)/ 100 + y;
            results[i] = stat;
        }
        return results;
    }
}
