package abstraction.eq3Transformateur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Variable;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;
import abstraction.fourni.Filiere;
/** @author AMAURY COUDRAY*/
public class Transformateur1 extends VendeurChocolat {
	public Transformateur1() {
		this.stockFeves.put(Feve.FEVE_MOYENNE,15.0);
		this.coutFeves.put(Feve.FEVE_MOYENNE, 6000.0);
		this.MontantCompte=500000.0;
		this.stockTotalFeves=new Variable("stock total de feves de "+getNom(),this,15.0);
		this.stockTotalChocolat=new Variable("stock total de chocolat de "+getNom(),this,0.0);
		this.stockTotalPateInterne=new Variable("stock total de pate interne de "+getNom(),this,0.0);

	}
	public Color getColor() {
		return new Color(52, 152, 219);
	}
	public void next() {
		for(Chocolat chocolat:this.getStockPateInterne().keySet()) {
			this.transformationPateChocolat(chocolat, this.getStockPateInterne(chocolat));
		}
		for(Feve feve:this.getStockFeves().keySet()) {
			this.transformationFevePate(feve, this.getStockFeves(feve));
		}
		System.out.println("cout des feves"+this.getCoutFeves());
		System.out.println("cout des pates internes"+this.getCoutPateInterne());
		System.out.println("cout des chocolat"+this.getCoutChocolat());
		System.out.println("stock chocoalt"+this.getStockChocolat());
	}
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.stockTotalFeves);
		res.add(this.stockTotalChocolat);
		res.add(this.stockTotalPateInterne);
		return res;
	}
	

}
