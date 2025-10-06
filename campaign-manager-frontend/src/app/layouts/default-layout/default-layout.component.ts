import { Component } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { IconModule } from '@coreui/icons-angular';

@Component({
  selector: 'app-default-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,
    IconModule
  ],
  templateUrl: './default-layout.component.html',
  styleUrl: './default-layout.component.scss'
})
export class DefaultLayoutComponent {
  
}