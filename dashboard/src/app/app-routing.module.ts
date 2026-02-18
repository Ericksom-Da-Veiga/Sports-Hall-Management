import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './view/dashboard/dashboard.component';
import { AdherantComponent } from './view/adherant/adherant.component';
import { AdherantEditComponent } from './view/adherant-edit/adherant-edit.component';
import { PayementComponent } from './view/payement/payement.component';
import { PayementEditComponent } from './view/payement-edit/payement-edit.component';
import { PayementAddComponent } from './view/payement-add/payement-add.component';
import { SportComponent } from './view/sport/sport.component';
import { MonitorsComponent } from './view/monitors/monitors.component';
import { MonitorsAddsComponent } from './view/monitors-adds/monitors-adds.component';
import { MonitorsEditComponent } from './view/monitors-edit/monitors-edit.component';
import { StatistiquesComponent } from './view/statistiques/statistiques.component';
import { LoginComponent } from './view/login/login.component';
import { SettingsComponent } from './view/settings/settings.component';
import { AdherantAddComponent } from './view/adherant-add/adherant-add.component';
import { SportEditComponent } from './view/sport-edit/sport-edit.component';
import { SportAddComponent } from './view/sport-add/sport-add.component';
import { AbonnementComponent } from './view/abonnement/abonnement.component';
import { AbonnementAddComponent } from './view/abonnement-add/abonnement-add.component';
import { EditAbonnementComponent } from './components/edit-abonnement/edit-abonnement.component';
import { AbonnementEditComponent } from './view/abonnement-edit/abonnement-edit.component';
import { AbonnementDetailComponent } from './view/abonnement-detail/abonnement-detail.component';
import { AuthGardService } from './services/auth-gard/auth-gard.service';
import { SettingsAddComponent } from './view/settings-add/settings-add.component';
import { SettingsEditComponent } from './view/settings-edit/settings-edit.component';
import { UsersComponent } from './view/users/users.component';
import { UsersAddComponent } from './view/users-add/users-add.component';
import { UsersEditComponent } from './view/users-edit/users-edit.component';
import { TableCurrentUserComponent } from './components/table-current-user/table-current-user.component';
import { ConfiguracaoComponent } from './view/configuracao/configuracao.component';

const routes: Routes = [
  {path: "dashboard", component: DashboardComponent, title: "Dashboard", canActivate: [AuthGardService], data: { role: 'Admin' }},
  {path: "", component: DashboardComponent, title: "Dashboard", canActivate: [AuthGardService], data: { role: 'Admin' }},

  {path: "adherant", component: AdherantComponent,title: "Adherant", canActivate: [AuthGardService]},
  {path: "adherant/:id/edit", component: AdherantEditComponent, title:"Edit adherant", canActivate: [AuthGardService]},
  {path: "adherant/add", component: AdherantAddComponent,title: "Ajouter Adherant", canActivate: [AuthGardService] },

  {path: "payement", component: PayementComponent, title: "Payements"},
  {path: "payement/:id/edit", component: PayementEditComponent, title:"Edit Payement", canActivate: [AuthGardService]},
  {path: "payement/add", component: PayementAddComponent,title: "Ajouter Payement", canActivate: [AuthGardService]},

  {path: "abonnement", component: AbonnementComponent, title: "Abonnements", canActivate: [AuthGardService]},
  {path: "abonnement/:id/edit", component: AbonnementEditComponent, title: "Edit Abonnement",canActivate: [AuthGardService] },
  {path: "abonnement/add", component: AbonnementAddComponent,title: "Ajouter Abonnement", canActivate: [AuthGardService] },
  {path: "abonnement/detail/:id", component: AbonnementDetailComponent, title: "Detail Abonnement", canActivate: [AuthGardService]},
  
  {path: "sports", component: SportComponent,title: "Sports", canActivate: [AuthGardService], data: { role: 'Admin' }},
  {path: "sports/:id/edit", component: SportEditComponent, title:"Edit Sports", canActivate: [AuthGardService]},
  {path: "sports/add", component: SportAddComponent,title: "Ajouter Sports", canActivate: [AuthGardService] },

  {path: "monitor", component: MonitorsComponent,title: "Monitor", canActivate: [AuthGardService]},
  {path: "monitor/add", component: MonitorsAddsComponent,title: "Ajouter Monitor", canActivate: [AuthGardService]},
  {path: "monitor/:id/edit", component: MonitorsEditComponent,title: "Edit Monitor", canActivate: [AuthGardService]},

  {path: "users", component: UsersComponent, title: "Utilizadores", canActivate: [AuthGardService], data: { role: 'Admin' }},
  {path: "users/add", component: UsersAddComponent, title: "Adicionar Utilizador", canActivate: [AuthGardService], data: { role: 'Admin' }},
  {path: "users/:id/edit", component: UsersEditComponent, title: "Modificar Utilizador", canActivate: [AuthGardService], /* still guarded but self-edit allowed */},

  // configuration page for non‑admins
  {path: "configuracao", component: ConfiguracaoComponent, title: "Configuração", canActivate: [AuthGardService]},

  {path: "statistique", component: StatistiquesComponent, title: "Statistiques", canActivate: [AuthGardService]},
  {path: "login", component: LoginComponent,title: "Login"},
  {path: "settings",component: SettingsComponent, title: "settings", canActivate: [AuthGardService], data: { role: 'SuperAdmin' }},
  
  {path: "user/add",component: SettingsAddComponent, title: "Add Users", canActivate: [AuthGardService]},
  {path: "user/:id/edit",component: SettingsEditComponent, title: "Modify User", canActivate: [AuthGardService]}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
