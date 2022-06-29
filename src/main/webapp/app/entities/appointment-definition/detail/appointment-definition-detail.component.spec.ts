import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppointmentDefinitionDetailComponent } from './appointment-definition-detail.component';

describe('Component Tests', () => {
  describe('AppointmentDefinition Management Detail Component', () => {
    let comp: AppointmentDefinitionDetailComponent;
    let fixture: ComponentFixture<AppointmentDefinitionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AppointmentDefinitionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ appointmentDefinition: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AppointmentDefinitionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppointmentDefinitionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appointmentDefinition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appointmentDefinition).toEqual(expect.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
