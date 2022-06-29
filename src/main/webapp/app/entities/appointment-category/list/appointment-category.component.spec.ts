import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AppointmentCategoryService } from '../service/appointment-category.service';

import { AppointmentCategoryComponent } from './appointment-category.component';

describe('Component Tests', () => {
  describe('AppointmentCategory Management Component', () => {
    let comp: AppointmentCategoryComponent;
    let fixture: ComponentFixture<AppointmentCategoryComponent>;
    let service: AppointmentCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentCategoryComponent],
      })
        .overrideTemplate(AppointmentCategoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentCategoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AppointmentCategoryService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 'ABC' }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.appointmentCategories?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
