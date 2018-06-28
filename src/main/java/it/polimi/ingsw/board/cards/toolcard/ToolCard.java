package it.polimi.ingsw.board.cards.toolcard;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.cards.Card;
import it.polimi.ingsw.board.cards.toolcard.effects.Effect;
import it.polimi.ingsw.board.cards.toolcard.effects.EffectType;
import it.polimi.ingsw.board.cards.toolcard.effects.Prerequisite;

import java.io.Serializable;
import java.util.ArrayList;

public class ToolCard extends Card implements Serializable {
	private int id;
	private String name;
	private ArrayList<Effect> effects;
	private ArrayList<Prerequisite> pres;
	private int favorTokensNumber;

	/**
	 * Constructs a tool card with a list of effects.
	 *
	 * @param id the id of the tool card
	 * @param name the name of the tool card
	 * @param effects the list of effects of the tool card
	 * @param pres the list of the prerequisites of the tool card
	 */
	public ToolCard(int id, String name, ArrayList<Effect> effects, ArrayList<Prerequisite> pres) {
		this.id = id;
		this.name = name;
		this.favorTokensNumber = 0;
		this.effects = effects;
		this.pres = pres;

	}

	public void reNew() {
		for(Effect effect : effects)
			effect.setUsed(false);
	}

	/**
	 * Inzitialize the effects.
	 * @param game the game to apply the effects to
	 */
	public void initializeEffects(Game game) {
		for(Effect effect : effects)
			effect.setGame(game);
	}

	/**
	 * Inzitialize the prerequisites.
	 * @param game the game in order to check its data
	 */
	public void initializePres(Game game){
		for(Prerequisite pre : pres)
			pre.setGame(game);
	}
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getFavorTokensNumber() {
		return favorTokensNumber;
	}

	public void setFavorTokensNumber(int favorTokensNumber) {
		this.favorTokensNumber = favorTokensNumber;
	}

	/**
	 *
	 * @param index the index of the requested effect
	 * @return the effect at the selectd index
	 */
	public Effect getEffect(int index) {
		return effects.get(index);
	}

	public ArrayList<Effect> getEffects() {
		return effects;
	}

	/**
	 * Return the index of the first unused effect
	 *
	 * @param effectType the effect type requested
	 * @return the index of the first unused effect if it is of the reqeusted type, or -1 otherwise
	 */
	public int validate(EffectType effectType) {
		int i;
		for(i = 0; i < effects.size(); i++) {
			if(! effects.get(i).isUsed()) {	//First unused
				if(effects.get(i).getMyEnum() == effectType)
					return i;
				else
					return - 1;
			}
		}

		return - 1;
	}

	/**
	 *
	 * @return the next unused effects, or null if all the effects are used
	 */
	public Effect getNext() {
		int i;
		for(i = 0; i < effects.size(); i++) {
			if(! effects.get(i).isUsed()) {
				return effects.get(i);
			}
		}

		return null;
	}

	/**
	 * Return the index of the last used effect of a specific type.
	 * @param effectEnum the effect type requested
	 * @return the index of the last unuesed effect of a requested type, or -1 if there isn't a used effect of the requested type
	 */
	public int alreadyAppliedEffect(EffectType effectEnum) {
		int mostRecentEffectEnum = - 1;
		for(int i = 0; i < effects.size(); i++) {
			if(effects.get(i).getMyEnum() == effectEnum && effects.get(i).isUsed()) {
				mostRecentEffectEnum = i;
			}
		}
		return mostRecentEffectEnum;
	}

	/**
	 * Return a tool card wothout effects (usefull to send to client)
	 * @return a tool card without effects
	 */
	public ToolCard getCleanClone() {
		return new ToolCard(this.id, this.name, null, null);
	}
}
