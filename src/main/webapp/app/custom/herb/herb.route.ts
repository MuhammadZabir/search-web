import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Herb } from '../search-by-image/model/herb.model';
import { HerbService } from './service/herb.service';
import { HerbComponent } from './herb.component';
import { HerbViewComponent } from './view/herb-view.component';
import { HerbCreateComponent } from './create/herb-create.component';
import { HerbDeletePopupComponent } from './delete/herb-delete.component';
import { IHerb } from 'app/shared/model/herb.model';

@Injectable({ providedIn: 'root' })
export class HerbPageResolve implements Resolve<IHerb> {
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
        path: 'herb-category',
        component: HerbComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'herb-category/:id/view',
        component: HerbViewComponent,
        resolve: {
            herb: HerbPageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'herb-category/new',
        component: HerbCreateComponent,
        resolve: {
            herb: HerbPageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'herb-category/:id/edit',
        component: HerbCreateComponent,
        resolve: {
            herb: HerbPageResolve
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
        path: 'herb-category/:id/delete',
        component: HerbDeletePopupComponent,
        resolve: {
            herb: HerbPageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'searchWebApp.herb.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
