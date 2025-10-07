import { Injectable, TemplateRef } from '@angular/core';
import { Observable, Subject } from 'rxjs';

export interface ConfirmationConfig {
  message: string;
  title?: string;
  confirmText?: string;
  cancelText?: string;
  confirmClass?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ConfirmationService {
  private confirmationSubject = new Subject<ConfirmationConfig>();
  private resultSubject = new Subject<boolean>();

  confirmation$ = this.confirmationSubject.asObservable();

  confirm(message: string, title: string = 'Confirm Action'): Observable<boolean> {
    this.confirmationSubject.next({
      message,
      title,
      confirmText: 'Confirm',
      cancelText: 'Cancel',
      confirmClass: 'btn-danger'
    });
    
    return new Observable(observer => {
      const subscription = this.resultSubject.subscribe(result => {
        observer.next(result);
        observer.complete();
        subscription.unsubscribe();
      });
    });
  }

  handleResult(confirmed: boolean) {
    this.resultSubject.next(confirmed);
  }
}