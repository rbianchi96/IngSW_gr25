package it.polimi.ingsw.board.cards.toolcard;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.cards.Card;
import it.polimi.ingsw.board.cards.toolcard.effects.Effect;
import it.polimi.ingsw.board.cards.toolcard.effects.EffectsEnum;

import java.io.Serializable;
import java.util.ArrayList;

public class ToolCard extends Card implements Serializable {
	private int id;
	private String name;
	//private ArrayList<EffectsEnum> effectsEnums;
	private ArrayList<Effect> effects;
	private int favorTokensNumber;
	//	private int totalSteps;
	private Game game;

	public ToolCard(int id, String name, ArrayList<Effect> effects) {
		this.id = id;
		this.name = name;
		this.favorTokensNumber = 0;
		if(effects != null) {
			this.effects = effects;	//TODO clone
			populateEffects();
		} else
			this.effects = null;

	}

	public void reNew() {
		for(Effect effect : effects)
			effect.setUsed(false);
	}

	public void populateEffects() {
		for(Effect effect : effects)
			effect.setGame(game);
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

	public ArrayList<Effect> getEffects() {
		return effects;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	// return the position in the effects'list of the requested effect if it can be "played" in this step, -1 if not
	public int validate(EffectsEnum effectsEnum) {
		int i;
		for(i = 0; i < effects.size(); i++) {
			if(! effects.get(i).isUsed()) {	//First unused
				if(effects.get(i).getMyEnum() == effectsEnum)
					return i;
				else
					return - 1;
			}
		}

		return - 1;
	}

	// return the next never used effect
	public Effect getNext() {
		int i;
		for(i = 0; i < effects.size(); i++) {
			if(! effects.get(i).isUsed()) {
				return effects.get(i);
			}
		}

		return null;
	}

	// return the position in the list of effects, of the most recent use of requested Effect, -1 if was never used
	public int alreadyAppliedEffect(EffectsEnum effectEnum) {
		int mostRecentEffectEnum = - 1;
		for(int i = 0; i < effects.size(); i++) {
			if(effects.get(i).getMyEnum() == effectEnum && effects.get(i).isUsed()) {
				mostRecentEffectEnum = i;
			}
		}
		return mostRecentEffectEnum;
	}

	// return a clone of a clean toolcard( without all the data saved in the effects)
	public ToolCard getCleanClone(){
		ArrayList<Effect> noneEffects = new ArrayList<>();
		noneEffects = null;
		ToolCard cleanToolCard = new ToolCard(this.id, this.name,noneEffects);
		return cleanToolCard;
	}
}
