import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Herb } from 'app/shared/model/herb.model';
import { HerbService } from './herb.service';
import { HerbComponent } from './herb.component';
import { HerbDetailComponent } from './herb-detail.component';
import { HerbUpdateComponent } from './herb-update.component';
import { HerbDeletePopupComponent } from './herb-delete-dialog.component';
import { IHerb } from 'app/shared/model/herb.model';

@Injectable({ providedIn: 'root' })
export class HerbResolve implements Resolve<IHerb> {
    constructor(private service: HerbService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((herb: HttpResponse<Herb>) => herb.body);
        }
        return Observable.of(new Herb());
    }
}

export const herbRoute: Routes = [
    {
        path: 'herb',
        component: HerbComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'herb/:id/view',
        component: HerbDetailComponent,
        resolve: {
            herb: HerbResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'herb/new',
        component: HerbUpdateComponent,
        resolve: {
            herb: HerbResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'herb/:id/edit',
        component: HerbUpdateComponent,
        resolve: {
            herb: HerbResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const herbPopupRoute: Routes = [
    {
        path: 'herb/:id/delete',
        component: HerbDeletePopupComponent,
        resolve: {
            herb: HerbResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
