jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAppointmentDefinition, AppointmentDefinition } from '../appointment-definition.model';
import { AppointmentDefinitionService } from '../service/appointment-definition.service';

import { AppointmentDefinitionRoutingResolveService } from './appointment-definition-routing-resolve.service';

describe('Service Tests', () => {
  describe('AppointmentDefinition routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AppointmentDefinitionRoutingResolveService;
    let service: AppointmentDefinitionService;
    let resultAppointmentDefinition: IAppointmentDefinition | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AppointmentDefinitionRoutingResolveService);
      service = TestBed.inject(AppointmentDefinitionService);
      resultAppointmentDefinition = undefined;
    });

    describe('resolve', () => {
      it('should return IAppointmentDefinition returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentDefinition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAppointmentDefinition).toEqual({ id: 'ABC' });
      });

      it('should return new IAppointmentDefinition if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentDefinition = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAppointmentDefinition).toEqual(new AppointmentDefinition());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AppointmentDefinition })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentDefinition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAppointmentDefinition).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
