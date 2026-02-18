import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import jwt_decode from 'jwt-decode';

import { AuthGardService } from './auth-gard.service';

describe('AuthGardService', () => {
  let service: AuthGardService;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('Router', ['navigate']);
    TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: spy }
      ]
    });
    service = TestBed.inject(AuthGardService);
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('allows access when token exists and role matches', () => {
    const fakeToken = 'fake';
    spyOn<any>(jwt_decode, 'default').and.returnValue({ role: 'Admin' });
    localStorage.setItem('token', fakeToken);
    const route: any = { data: { role: 'Admin' } };
    expect(service.canActivate(route, null as any)).toBeTrue();
  });

  it('redirects non-admin to configuracao when requesting admin route', () => {
    const fakeToken = 'fake';
    spyOn<any>(jwt_decode, 'default').and.returnValue({ role: 'User' });
    localStorage.setItem('token', fakeToken);
    const route: any = { data: { role: 'Admin' } };
    const can = service.canActivate(route, null as any);
    expect(can).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/configuracao']);
  });

  it('redirects to login when token missing', () => {
    localStorage.removeItem('token');
    const route: any = { data: {} };
    const can = service.canActivate(route, null as any);
    expect(can).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
