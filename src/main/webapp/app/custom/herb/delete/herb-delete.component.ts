import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Herb } from '../../herb/model/herb.model';
import { HerbService } from '../service/herb.service';

@Component({
    selector: 'jhi-herb-delete',
    templateUrl: './herb-delete.component.html'
})
export class HerbDeleteComponent {
    herb: Herb;

    constructor(private herbService: HerbService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.herbService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'herbListModification',
                content: 'Deleted an herb'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-herb-delete-popup',
    template: ''
})
export class HerbDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ herb }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HerbDeleteComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.herb = herb;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
