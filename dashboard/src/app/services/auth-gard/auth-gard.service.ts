import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthGardService implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const authToken = localStorage.getItem('token'); // Usar localStorage ao invés de sessionStorage
    
    if (authToken) {
      const tokenPayload: any = jwt_decode(authToken);
      const requiredRole = next.data['role'];
      
      if (requiredRole && tokenPayload.role !== requiredRole) {
        // special case: user editing their own profile should be permitted
        const idParam = next.params ? next.params['id'] : null;
        if (requiredRole === 'Admin' && idParam != null) {
          const tokenId = tokenPayload.id ?? tokenPayload.sub ?? null;
          if (tokenId != null && String(tokenId) === String(idParam)) {
            return true; // allow self-edit
          }
        }
        // redirect non‑admins requesting admin-area to configuration page
        if (requiredRole === 'Admin') {
          this.router.navigate(['/configuracao']);
        } else {
          this.router.navigate(['/dashboard']); // generic redirect
        }
        return false;
      }
      return true;
    } else {
      this.router.navigate(['/login']);

      return false;
    }
  }

}
