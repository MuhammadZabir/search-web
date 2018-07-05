/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SearchWebTestModule } from '../../../test.module';
import { HerbDeleteDialogComponent } from 'app/entities/herb/herb-delete-dialog.component';
import { HerbService } from 'app/entities/herb/herb.service';

describe('Component Tests', () => {
    describe('Herb Management Delete Component', () => {
        let comp: HerbDeleteDialogComponent;
        let fixture: ComponentFixture<HerbDeleteDialogComponent>;
        let service: HerbService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SearchWebTestModule],
                declarations: [HerbDeleteDialogComponent]
            })
                .overrideTemplate(HerbDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HerbDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HerbService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
