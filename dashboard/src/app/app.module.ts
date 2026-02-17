import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';

import { NgxPaginationModule } from 'ngx-pagination';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TableAdherantsComponent } from './components/table-adherants/table-adherants.component';
import { DashboardComponent } from './view/dashboard/dashboard.component';
import { DashboardCardsComponent } from './components/dashboard-cards/dashboard-cards.component';
import { DashboardTableComponent } from './components/dashboard-table/dashboard-table.component';
import { FormPayementComponent } from './components/form-payement/form-payement.component';
import { FormAdherantComponent } from './components/form-adherant/form-adherant.component';
import { FormSportComponent } from './components/form-sport/form-sport.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { TableMonitorsComponent } from './components/table-monitors/table-monitors.component';
import { TablePayementsComponent } from './components/table-payements/table-payements.component';
import { TableSportsComponent } from './components/table-sports/table-sports.component';
import { FormMonitorComponent } from './components/form-monitor/form-monitor.component';
import { AdherantComponent } from './view/adherant/adherant.component';
import { AdherantEditComponent } from './view/adherant-edit/adherant-edit.component';
import { AdherantAddComponent } from './view/adherant-add/adherant-add.component';
import { SportComponent } from './view/sport/sport.component';
import { SportEditComponent } from './view/sport-edit/sport-edit.component';
import { SportAddComponent } from './view/sport-add/sport-add.component';
import { PayementComponent } from './view/payement/payement.component';
import { PayementEditComponent } from './view/payement-edit/payement-edit.component';
import { PayementAddComponent } from './view/payement-add/payement-add.component';
import { MonitorsComponent } from './view/monitors/monitors.component';
import { MonitorsEditComponent } from './view/monitors-edit/monitors-edit.component';
import { MonitorsAddsComponent } from './view/monitors-adds/monitors-adds.component';
import { LoginComponent } from './view/login/login.component';
import { SettingsComponent } from './view/settings/settings.component';
import { StatistiquesComponent } from './view/statistiques/statistiques.component';
import { AbonnementComponent } from './view/abonnement/abonnement.component';
import { AbonnementAddComponent } from './view/abonnement-add/abonnement-add.component';
import { FormAbonnementComponent } from './components/form-abonnement/form-abonnement.component';
import { TableAbonnementComponent } from './components/table-abonnement/table-abonnement.component';
import { FormsModule } from '@angular/forms';
import { EditAdherantComponent } from './components/edit-adherant/edit-adherant.component';
import { EditCoachComponent } from './components/edit-coach/edit-coach.component';
import { EditSportComponent } from './components/edit-sport/edit-sport.component';
import { EditAbonnementComponent } from './components/edit-abonnement/edit-abonnement.component';
import { EditPayementComponent } from './components/edit-payement/edit-payement.component';
import { AbonnementEditComponent } from './view/abonnement-edit/abonnement-edit.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AbonnementDetailComponent } from './view/abonnement-detail/abonnement-detail.component';
import { AbonnementInformationsComponent } from './components/abonnement-informations/abonnement-informations.component';
import { TableAdminComponent } from './components/table-admin/table-admin.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { TableUsersComponent } from './components/table-users/table-users.component';
import { SettingsEditComponent } from './view/settings-edit/settings-edit.component';
import { SettingsAddComponent } from './view/settings-add/settings-add.component';
import { EditSettingsComponent } from './components/edit-settings/edit-settings.component';
import { FormSettingsComponent } from './components/form-settings/form-settings.component';
import { ChatComponent } from './components/chat/chat.component';
import { UsersComponent } from './view/users/users.component';
import { UsersAddComponent } from './view/users-add/users-add.component';
import { UsersEditComponent } from './view/users-edit/users-edit.component';
import { FormUsersComponent } from './components/form-users/form-users.component';
import { EditUsersComponent } from './components/edit-users/edit-users.component';


@NgModule({
  declarations: [
    AppComponent,
    TableAdherantsComponent,
    DashboardComponent,
    DashboardCardsComponent,
    DashboardTableComponent,
    FormPayementComponent,
    FormAdherantComponent,
    FormSportComponent,
    NavbarComponent,
    SidenavComponent,
    TableMonitorsComponent,
    TablePayementsComponent,
    TableSportsComponent,
    FormMonitorComponent,
    AdherantComponent,
    AdherantEditComponent,
    AdherantAddComponent,
    SportComponent,
    SportEditComponent,
    SportAddComponent,
    PayementComponent,
    PayementEditComponent,
    PayementAddComponent,
    MonitorsComponent,
    MonitorsEditComponent,
    MonitorsAddsComponent,
    LoginComponent,
    SettingsComponent,
    StatistiquesComponent,
    AbonnementComponent,
    AbonnementAddComponent,
    FormAbonnementComponent,
    TableAbonnementComponent,
    EditAdherantComponent,
    EditCoachComponent,
    EditSportComponent,
    EditAbonnementComponent,
    EditPayementComponent,
    AbonnementEditComponent,
    AbonnementDetailComponent,
    AbonnementInformationsComponent,
    TableAdminComponent,
    TableUsersComponent,
    SettingsEditComponent,
    SettingsAddComponent,
    EditSettingsComponent,
    FormSettingsComponent,
    ChatComponent,
    UsersComponent,
    UsersAddComponent,
    UsersEditComponent,
    FormUsersComponent,
    EditUsersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    NgxPaginationModule,
    NgMultiSelectDropDownModule.forRoot(),
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
