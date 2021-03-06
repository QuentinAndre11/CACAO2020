package abstraction.eq5Transformateur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import abstraction.eq8Romu.cacaoCriee.LotCacaoCriee;
import abstraction.eq8Romu.cacaoCriee.PropositionCriee;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;
import abstraction.fourni.Variable;

/** @author Eva DUPUY 
 * Classe qui régit tous les achats de fèves, à la criée et aléatoire
 * Elle est agrégé dans la classe Transformateur3.
 */
public class AchatCacao {
	//Ces nombres influencent le prix proposé dans proposer achat
		private int NB_propositions_refusees;
		private int NB_precedent;
		private double prix; 
		private Transformateur3 acteur;
		private Map<Feve, List<Couple<Variable>>> tentativeDachat;
		
		public AchatCacao(Transformateur3 acteur) {
			this.acteur = acteur;
			this.NB_propositions_refusees = 0;
			this.NB_precedent = 0;
			tentativeDachat = new HashMap<>();
	}
	
	// achète le lot au prix minimum si les feves sont haut de gamme et equitables
	// Il faut ajouter une condition sur les stocks et la trésorerie
	
	public double proposerAchat(LotCacaoCriee lot) {
		if (tentativeDachat.get(lot.getFeve()) == null) {
			tentativeDachat.put(lot.getFeve(),new LinkedList<>());
		} 
		
		else {
			tentativeDachat.get(lot.getFeve()).clear();
		}
		//On verifie si le lot est du bon type de chocolat et si on en a pas déjà trop en stock
		if (lot.getFeve().isEquitable() && lot.getFeve().getGamme()== Gamme.HAUTE && 
				lot.getQuantiteEnTonnes() + this.acteur.getStock().getQuantiteChocolat(lot.getFeve()) <= 200) {

			prix = lot.getPrixMinPourUneTonne();
			if (NB_propositions_refusees == NB_precedent && acteur.getTresorier()
					.investissementMaxCompteSecondaire() >= (prix
							+ this.acteur.getStock().getTransformationCostFeve().getValeur())
							* lot.getQuantiteEnTonnes()) {
				tentativeDachat.get(lot.getFeve()).add(new Couple<Variable>(
						new Variable("", acteur, lot.getQuantiteEnTonnes()), new Variable("", acteur, prix)));
				return prix; //vrai que pour l'initialisation
			}

			else if (NB_propositions_refusees > NB_precedent && acteur.getTresorier()
					.investissementMaxCompteSecondaire() >= (prix
							+ this.acteur.getStock().getTransformationCostFeve().getValeur())
							* lot.getQuantiteEnTonnes()) { //derniere proposition acceptee
				tentativeDachat.get(lot.getFeve()).add(new Couple<Variable>(
						new Variable("", acteur, lot.getQuantiteEnTonnes()), new Variable("", acteur, prix)));
				return prix;
			}

			//(NB_propositions_refusees < NB_precedent) la dernière proposition a été refusée
			else {
				// 1.5 est un coefficient choisi au hasard pour debuter
				prix = prix * (1 + NB_propositions_refusees) * 1.5;
				if (acteur.getTresorier()
						.investissementMaxCompteSecondaire() >= (prix
								+ this.acteur.getStock().getTransformationCostFeve().getValeur())
								* lot.getQuantiteEnTonnes()) {
					tentativeDachat.get(lot.getFeve()).add(new Couple<Variable>(
							new Variable("", acteur, lot.getQuantiteEnTonnes()), new Variable("", acteur, prix)));
					return prix; //plus on a de prop refusees, plus on augmente le prix

				} else {
					return 0.0;
				}
			}
		}
		// Sinon on regarde si les marges possibles sont très grandes, si c'est le cas on peut décider d'en acheter un peu
		else {
			return 0.0;
		}
	}
	
	
	public void notifierPropositionRefusee(PropositionCriee proposition) {
		NB_precedent = NB_propositions_refusees;
		NB_propositions_refusees += 1;
	}	
	
	//diminue le nombre de propositions refusées donc on peut diminuer le prix de proposition
	//ajoute les feves du lot au stock de feves de l'entprise
	
	public void notifierVente(PropositionCriee proposition) {
		NB_precedent = NB_propositions_refusees;
		NB_propositions_refusees = Math.max(NB_propositions_refusees - 1, 0);
		
		this.acteur.getStock().ajoutFeves(proposition.getFeve(), 
				tentativeDachat.get(proposition.getFeve()).get(0).get1().getValeur(),
				tentativeDachat.get(proposition.getFeve()).get(0).get2().getValeur());
		if(proposition.getFeve() == Feve.FEVE_HAUTE){
		this.acteur.getTresorier().jaiAcheteSecondaire( 
				tentativeDachat.get(proposition.getFeve()).get(0).get1().getValeur()*
				tentativeDachat.get(proposition.getFeve()).get(0).get2().getValeur());
		} else if(proposition.getFeve() == Feve.FEVE_HAUTE_EQUITABLE){
		this.acteur.getTresorier().jaiAchetePrincipale( 
			tentativeDachat.get(proposition.getFeve()).get(0).get1().getValeur()*
			tentativeDachat.get(proposition.getFeve()).get(0).get2().getValeur());
		}
	}
	
	
	
	
	
	
	
}