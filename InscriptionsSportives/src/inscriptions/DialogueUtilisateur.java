package inscriptions;

import java.awt.List;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.SortedSet;

import commandLineMenus.Option;
import commandLineMenus.rendering.examples.util.InOut;
import commandLineMenus.Action;
import commandLineMenus.Menu;


public class DialogueUtilisateur {

	private static Inscriptions inscriptions;
	private Personne pers;
	private Competition compet;
	private Equipe team;
	final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	

	public DialogueUtilisateur(Inscriptions inscriptions) {
		DialogueUtilisateur.inscriptions = inscriptions;
	}
	
	public void autoSave() {
		try
		{
			inscriptions.sauvegarder();
		} 
		catch (IOException e)
		{
			System.out.println("Sauvegarde impossible." + e);
		}
	}
	
	public Menu MenuPrincipal() {
		
	Menu menu = new Menu("Menu !");
	
	menu.add(menuEquipe());
	menu.add(menuPersonne());
	menu.add(menuCompetition());
	menu.addQuit("4");
	return menu;
	
	}
	
	public void start() {
		
		MenuPrincipal().start();
	}
	
	private Menu menuCompetition() {
		Menu menuCompetition = new Menu("Comp�tition", "3");
		
		menuCompetition.add(creerCompet());
		menuCompetition.add(listCompetOption());
		menuCompetition.add(addTeamInCompetOption());
		menuCompetition.add(addGuyInCompetOption());
//		menuCompetition.add(editNameCompetOption());
//		menuCompetition.add(removeCompetOption());
		menuCompetition.addBack("b");
		return menuCompetition;
	}
	
	public Option creerCompet() {
		return new Option("Cr�er une comp�tition", "1", createCompetAction());
	}
	
	private Action createCompetAction() {
		return new Action() {
			public void optionSelected() {
				String nomCompet = InOut.getString("Entrer le nom de la comp�tition : ");
				String dateCloture = InOut.getString("Entrer la date de cl�ture : ");
				final LocalDate localDate = LocalDate.parse(dateCloture, DATE_FORMAT);
				String teamOrNotTeam = null;
				boolean enEquipe = false;
				
				/* 
				do {
					teamOrNotTeam = InOut.getString("Entrer 'equipe' pour une comp�tition en �quipe ? (equipe) ");
				}while(!teamOrNotTeam.equals("equipe"));	
				
				if(teamOrNotTeam.equals("equipe")) {
					enEquipe = true;
				}
				else {
					enEquipe = false;
				}
				*/
				Competition compet = inscriptions.createCompetition(nomCompet, localDate, enEquipe);
				autoSave();
				System.out.println("La comp�tition, " + nomCompet + " a �tait cr��e avec succ�s");
			}
		};
	}
	
	public Option listCompetOption() {
		return new Option("Lister les comp�titions", "2", listCompetAction());
	}
	
	private Action listCompetAction() {
		return new Action() {
			public void optionSelected() {
				System.out.println(inscriptions.getCompetitions());
			}
		};
	}
	
	public Option addTeamInCompetOption() {
		return new Option("Ajouter une �quipe dans une comp�tition", "3", addTeamInCompetAction());
	}
	
	private Action addTeamInCompetAction() {
		return new Action () {
			public void optionSelected() {
				
				String nameCompet = InOut.getString("Entrer le nom de la comp�tition : ");
				SortedSet <Candidat> listTeams = inscriptions.getCandidats();
				SortedSet <Competition> listCompet = inscriptions.getCompetitions();
				SortedSet <Equipe> listTeam = inscriptions.getEquipes();
				String nameTeam = null;
				
				do {
				
					for(Competition co : listCompet) {
						if(co.getNom().equals(nameCompet)) {
							
							nameTeam = InOut.getString("Entrer le nom d'une �quipe : ");
							for(Candidat c : listTeams) {
								if(c.getNom().equals(nameTeam)) {
									for(Equipe t : listTeam) {
										if(t.toString().equals(c.toString())) {
											co.add(t);
											autoSave();
											System.out.println(nameTeam + "est inscrite dans la comp�tition " + nameCompet);
										}
									}
								}
							}
						}
		
							
						
					}
				
				}while(!nameTeam.equals("stop"));
				
			}
		};
	}
	
	public Option addGuyInCompetOption() {
		return new Option("Ajouter une personne dans une comp�tition", "4", addGuyInCompetAction());
	}
	
	private Action addGuyInCompetAction() {
		return new Action () {
			public void optionSelected() {
				String nameCompet = InOut.getString("Entrer le nom de la comp�tition : ");
				SortedSet <Candidat> listTeams = inscriptions.getCandidats();
				SortedSet <Competition> listCompet = inscriptions.getCompetitions();
				SortedSet <Personne> listGuys = inscriptions.getPersonnes();
				String nameGuy = null;
				
				do {
				
					for(Competition co : listCompet) {
						if(co.getNom().equals(nameCompet)) {
							nameGuy = InOut.getString("Entrer le nom d'un sportif : ");
							for(Candidat c : listTeams) {
								if(c.getNom().equals(nameGuy)) {
									for(Personne p : listGuys) {
										if(p.getNom().equals(nameGuy)) {
											co.add(p);
											autoSave();
											System.out.println(nameGuy + "est inscrite dans la comp�tition " + nameCompet);
										}
									}
								}
							}
						}
					}
				
				}while(!nameGuy.equals("stop"));
			}
		};
	}
	
	private Menu menuEquipe() {
		Menu menuEquipe = new Menu("Equipe", "1");
		menuEquipe.add(createTeamOption());
		menuEquipe.add(listTeamOption());
		menuEquipe.add(listMemberTeamOption());
		menuEquipe.add(removeTeamOption());
		menuEquipe.add(editNameTeamOption());
		menuEquipe.add(removeGuyOfTeamOption());
		menuEquipe.addBack("b");
		return menuEquipe;
	}
	
	public Option createTeamOption() {
		return new Option("Cr�er une �quipe", "1", createTeamAction());
	}
	
	private Action createTeamAction() {
		return new Action() {
			public void optionSelected() {
				String nomEquipe = InOut.getString("Entrer le nom de l'�quipe : ");
				Equipe team = inscriptions.createEquipe (nomEquipe);
				System.out.println("L'�quipe, " + nomEquipe + " a �tait cr��e avec succ�s");
				autoSave();
			}
		};
	}
	
	public Option listTeamOption() {
		return new Option("Lister les �quipes", "2", listTeamAction());
	}
	
	private Action listTeamAction() {
		return new Action() {
			public void optionSelected() {
				System.out.println(inscriptions.getEquipes());
			}
		};
	}
	
	public Option listMemberTeamOption() {
		return new Option("Lister les membres d'une �quipe", "3", listMemberTeamAction());
	}
	
	private Action listMemberTeamAction() {
		return new Action() {
			public void optionSelected() {
				String nameTeam = InOut.getString("Nom de l'�quipe : ");
				SortedSet<Equipe> listTeam = inscriptions.getEquipes();
				SortedSet<Candidat> listTeams = inscriptions.getCandidats();
				
				for(Candidat c : listTeams) {
					if(c.getNom().equals(nameTeam)) {
						for(Equipe t : listTeam) {
							if(t.toString().equals(c.toString())) {
								System.out.println(t.getMembres());
							}
						}
					}
				}
			}
		};
	}
	
	public Option removeTeamOption() {
		return new Option("Supprimer une �quipe", "4", removeTeamAction());
	}
	
	private Action removeTeamAction() {
		return new Action() {
			public void optionSelected() {
				String nameTeam = InOut.getString("Nom de l'�quipe : ");
				boolean deleteSuccess = false;
				SortedSet<Candidat> listTeams = inscriptions.getCandidats();
				
				for(Candidat c : listTeams) {
					
					if(c.getNom().equals(nameTeam)) {
						c.delete();
						System.out.println(c.getNom() + ", a bien �tait supprim�e");
						autoSave();
						deleteSuccess = true;
						break;
					}
				}
				
				if(!deleteSuccess) {
					System.out.println("La suppression a �chou�, car l'�quipe n'est pas r�pertori�e");
					
				}
			}
		};
	}
	
	public Option editNameTeamOption() {
		return new Option("Editer le nom d'une �quipe", "5", editNameTeamAction());
	}
	
	private Action editNameTeamAction() {
		return new Action() {
			public void optionSelected() {
				String nameTeam = InOut.getString("Nom de l'�quipe : ");
				SortedSet<Candidat> listTeams = inscriptions.getCandidats();
				
				for(Candidat c : listTeams) {
					
					if(c.getNom().equals(nameTeam)) {
						String newName = InOut.getString("Nouveau de l'�quipe : ");
						c.setNom(newName);
						autoSave();
						System.out.println("Le nouveau nom de l'�quipe : " + nameTeam + "est : " + newName);
						break;
					}
				}
			}
		};
	}
	
	public Option removeGuyOfTeamOption() {
		return new Option("Supprimer un sportif d'une �quipe", "6", removeGuyOfTeamAction());
	}
	
	private Action removeGuyOfTeamAction() {
		return new Action() {
			public void optionSelected() {
				String nameTeam = InOut.getString("Nom de l'�quipe : ");
				boolean deleteSuccess = false;
				SortedSet<Candidat> listTeams = inscriptions.getCandidats();
				SortedSet<Personne> listGuys = inscriptions.getPersonnes();
				SortedSet<Equipe> listTeam = inscriptions.getEquipes();
				String nomPersonne = null;
				
				do {
					for(Candidat c : listTeams) {
					
						if(c.getNom().equals(nameTeam)) {
							nomPersonne = InOut.getString("Entrer le nom du sportif � supprimer : ");
							for(Personne p : listGuys) {
								if(p.getNom().equals(nomPersonne)) {
									for(Equipe t : listTeam) {
										if(t.toString().equals(c.toString())) {
											t.remove(p);
											System.out.println(p.getNom() + " " + p.getPrenom() + ", a bien �tait supprim�e de : " + c.getNom());
											autoSave();
											deleteSuccess = true;
											break;
										}
									}
								}
							}
						}
					}
				
					if(!deleteSuccess) {
						System.out.println("La suppression a �chou�, car le sportif n'appartient pas  n'est pas r�pertori�e");
					}
				}while(!nomPersonne.equals("stop"));
			}
		};
	}
	
	private Menu menuPersonne() {
		Menu menuPersonne = new Menu("Personne", "2");
		menuPersonne.add(addAGuyOption());
		menuPersonne.add(listGuysOption());
		menuPersonne.add(removeGuyOption());
		menuPersonne.add(menuEditGuy());
		menuPersonne.add(addAGuyInTeamOption());
		menuPersonne.addBack("b");
		return menuPersonne;
	}
	
	public Option addAGuyOption() {
		
		return new Option("Ajouter un sportif", "1", addAGuyAction());
	}
	
	private Action addAGuyAction() {
		
		return new Action ()
		{
			public void optionSelected()
			{
				String nomPersonne = InOut.getString("Nom : ");
				String prenomPersonne = InOut.getString("Prenom : ");
				String mailPersonne = InOut.getString("Mail : ");
				Personne pers = inscriptions.createPersonne(nomPersonne, prenomPersonne, mailPersonne);
				System.out.println(pers.getNom() + " " + pers.getPrenom() + ", a �tait cr��(e) avec succ�s" + " son mail est : " + pers.getMail());
				autoSave();
			}
		};
	}

	public Option listGuysOption() {
		
		return new Option("Lister les sportifs", "2", listGuysAction());
	}

	private Action listGuysAction() {
		
		return new Action() {
			public void optionSelected() {
				System.out.println(inscriptions.getPersonnes());
			}
		};
	}
	
	public Option removeGuyOption() {
		return new Option("Supprimer un sportif", "3", removeGuyAction());
	}
	
	private Action removeGuyAction() {
		return new Action() {
			public void optionSelected() {
				String mailPersonne = InOut.getString("Mail : ");
				String nomPersonne = null;
				String prenomPersonne = null;
				boolean deleteSuccess = false;
				SortedSet<Personne> listGuys = inscriptions.getPersonnes();
				
				for(Personne p : listGuys) {
					
					if(p.getMail().equals(mailPersonne)) {
						p.delete();
						nomPersonne = p.getNom();
						prenomPersonne = p.getPrenom();
						System.out.println(nomPersonne + " " + prenomPersonne + ", a bien �tait supprim�(e)");
						autoSave();
						deleteSuccess = true;
						break;
					}
				}
				
				if(!deleteSuccess) {
					System.out.println("La suppression a �chou�, car le sportif n'est pas inscrit");
					
				}
					
			}
			
		};
	}
	
	public Menu menuEditGuy() {
		Menu menuEditGuy = new Menu ("Editer un sportif", "4");
		menuEditGuy.add(editNameOption());
		menuEditGuy.add(editLastNameOption());
		menuEditGuy.add(editMailOption());
		menuEditGuy.addBack("b");
		return menuEditGuy;
	}
	
	public Option editNameOption() {
		return new Option("Editer le nom", "1", editNameAction());
	}
	
	public Action editNameAction() {
		return new Action () {
			public void optionSelected() {
				String mailPersonne = InOut.getString("Mail : ");
				String nomPersonne = null;
				SortedSet<Personne> listGuys = inscriptions.getPersonnes();
				
				for(Personne p : listGuys) {
					
					if(p.getMail().equals(mailPersonne)) {
						nomPersonne = p.getNom();
						String prenomPersonne = p.getPrenom();
						System.out.println("Vous vous appr�tez � modifier : " + nomPersonne + " " + prenomPersonne);
						nomPersonne  = InOut.getString("Entrer le nouveau nom : ");
						String checkEdit = InOut.getString(nomPersonne + " " + prenomPersonne + ", vous validez la modification ? (Y/N) ");

						if(checkEdit.equals("y") || checkEdit.equals("Y") || checkEdit.equals("o") || checkEdit.equals("O")) {
							p.setNom(nomPersonne);
							System.out.println(nomPersonne + " " + prenomPersonne + ", le nom a bien �tait modifi�");
							autoSave();
							break;
						}
						else {
							break;
						}
					}
				}
			}
		};
	}
	
	public Option editLastNameOption() {
		return new Option("Editer le prenom", "2", editLastNameAction());
	}
	
	public Action editLastNameAction() {
		return new Action () {
			public void optionSelected() {
				String mailPersonne = InOut.getString("Mail : ");
				String prenomPersonne = null;
				SortedSet<Personne> listGuys = inscriptions.getPersonnes();
				
				for(Personne p : listGuys) {
					
					if(p.getMail().equals(mailPersonne)) {
						prenomPersonne = p.getPrenom();
						String nomPersonne = p.getNom();
						System.out.println("Vous vous appr�tez � modifier : " + nomPersonne + " " + prenomPersonne);
						prenomPersonne  = InOut.getString("Entrer le nouveau prenom : ");
						String checkEdit = InOut.getString(nomPersonne + " " + prenomPersonne + ", vous validez la modification ? (Y/N) ");

						if(checkEdit.equals("y") || checkEdit.equals("Y") || checkEdit.equals("o") || checkEdit.equals("O")) {
							p.setPrenom(prenomPersonne);
							System.out.println(nomPersonne + " " + prenomPersonne + ", le prenom a bien �tait modifi�");
							autoSave();
							break;
						}
						else {
							break;
						}
					}
				}
			}
		};
	}
	
	public Option editMailOption() {
		return new Option("Editer le mail", "3", editMailAction());
	}
	
	public Action editMailAction() {
		return new Action () {
			public void optionSelected() {
				String mailPersonne = InOut.getString("Mail : ");
				SortedSet<Personne> listGuys = inscriptions.getPersonnes();
				
				for(Personne p : listGuys) {
					
					if(p.getMail().equals(mailPersonne)) {
						String nomPersonne = p.getNom();
						String prenomPersonne = p.getPrenom();
						System.out.println("Vous vous appr�tez le mail de " + nomPersonne + " " + prenomPersonne);
						mailPersonne  = InOut.getString("Entrer le nouveau mail : ");
						String checkEdit = InOut.getString(mailPersonne + ", vous validez la modification ? (Y/N) ");

						if(checkEdit.equals("y") || checkEdit.equals("Y") || checkEdit.equals("o") || checkEdit.equals("O")) {
							p.setMail(mailPersonne);
							System.out.println("Le mail de : " + nomPersonne + " " + prenomPersonne + "a bien �t� modifi�");
							autoSave();
							break;
						}
						else {
							break;
						}
					}
				}
			}
		};
	}
	
	public Option addAGuyInTeamOption() {
		return new Option("Ajouter un sportif dans une �quipe", "5", addAGuyInTeamAction());
	}
	
	private Action addAGuyInTeamAction() {
		return new Action() {
			public void optionSelected() {
				SortedSet <Candidat> listTeams = inscriptions.getCandidats();
				SortedSet <Equipe> listTeam = inscriptions.getEquipes();
				System.out.println(inscriptions.getEquipes());
				String selectTeam = InOut.getString("Selectionner l'�quipe : ");
				String nomPersonne = null;
				SortedSet <Personne> listGuys = inscriptions.getPersonnes();
				System.out.println(inscriptions.getPersonnes());
				
				do {
					nomPersonne = InOut.getString("Nom du sportif : ");
					for(Candidat c : listTeams) {
						if(c.getNom().equals(selectTeam)) {
							for(Equipe t : listTeam) {
								if(t.toString().equals(c.toString())) {
									
									for(Personne p : listGuys) {
										if(p.getNom().equals(nomPersonne)) {
											t.add(p);
											autoSave();
											System.out.println("Le sportif : " + p.getNom() + " " + p.getPrenom() + " a rejoint l'�quipe " + c.getNom());
										}
									}
								}
								else {
									System.out.println("Il y a eu une erreur lors de l'ajout du sportif dans l'�quipe :" + c.getNom());
								}
							}
							
						}
					}	
				}while(!nomPersonne.equals("stop"));
			}
		};
	}
}