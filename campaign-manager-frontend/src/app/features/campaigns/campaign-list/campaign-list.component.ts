import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule, GridModule, TableModule, ButtonModule, ButtonDirective, ContainerComponent } from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';

@Component({
  selector: 'app-campaign-list',
  standalone: true,
  imports: [CommonModule, CardModule, GridModule, TableModule, ButtonModule, ButtonDirective, ContainerComponent, IconModule],
  templateUrl: './campaign-list.component.html',
  styleUrl: './campaign-list.component.scss'
})
export class CampaignListComponent {

}