package kavinex;

public class KarstasPatiekalas extends RuosiamasPatiekalas {

	public int trukme_kaitinimo;
	
	public KarstasPatiekalas() {
		
	}
	
	public KarstasPatiekalas (String pavadinimas, int trukme_ruosimo, int trukme_kaitinimo ) {
		
		super ( pavadinimas, trukme_ruosimo );
		this.trukme_kaitinimo = trukme_kaitinimo;
		bus_patiektas_apytiksliai_uz += trukme_kaitinimo;
	}
	
	public String toString() {
		
		return 
				this.pavadinimas 
			+ 	"/" 
			+ 	this.trukme_ruosimo 
			+ 	"/" 
			+ 	this.trukme_kaitinimo 
			+ 	";"
		;
	}	
	
	public boolean papildomiPalyginimai (Object obj) {
		
		boolean iret = false;
		
		System.out.println ( obj.getClass().getName() );
		
		if ( obj.getClass().getName().equals("kavinex.KarstasPatiekalas") ) {
		
			KarstasPatiekalas palyginimui = (KarstasPatiekalas) obj;
			/*
			System.out.println ( 
						this.trukme_kaitinimo + "==" + palyginimui.trukme_kaitinimo
					+ " yra "
						 + ( this.trukme_kaitinimo == palyginimui.trukme_kaitinimo )
			);
			*/
			iret =
						super.papildomiPalyginimai(obj)
					 &&	
						( this.trukme_kaitinimo == palyginimui.trukme_kaitinimo )
					;					
			
		} 
		return
				iret;
			
	}	
}
