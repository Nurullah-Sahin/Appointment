jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAppointmentSlot, AppointmentSlot } from '../appointment-slot.model';
import { AppointmentSlotService } from '../service/appointment-slot.service';

import { AppointmentSlotRoutingResolveService } from './appointment-slot-routing-resolve.service';

describe('Service Tests', () => {
  describe('AppointmentSlot routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AppointmentSlotRoutingResolveService;
    let service: AppointmentSlotService;
    let resultAppointmentSlot: IAppointmentSlot | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AppointmentSlotRoutingResolveService);
      service = TestBed.inject(AppointmentSlotService);
      resultAppointmentSlot = undefined;
    });

    describe('resolve', () => {
      it('should return IAppointmentSlot returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentSlot = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAppointmentSlot).toEqual({ id: 'ABC' });
      });

      it('should return new IAppointmentSlot if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentSlot = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAppointmentSlot).toEqual(new AppointmentSlot());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AppointmentSlot })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentSlot = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAppointmentSlot).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
