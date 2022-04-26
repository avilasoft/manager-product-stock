import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBatchStatus } from 'app/shared/model/batch-status.model';
import { getEntities as getBatchStatuses } from 'app/entities/batch-status/batch-status.reducer';
import { IBusinessAssociate } from 'app/shared/model/business-associate.model';
import { getEntities as getBusinessAssociates } from 'app/entities/business-associate/business-associate.reducer';
import { IBatchProduct } from 'app/shared/model/batch-product.model';
import { getEntities as getBatchProducts } from 'app/entities/batch-product/batch-product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './batch.reducer';
import { IBatch } from 'app/shared/model/batch.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BatchUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const batchStatuses = useAppSelector(state => state.batchStatus.entities);
  const businessAssociates = useAppSelector(state => state.businessAssociate.entities);
  const batchProducts = useAppSelector(state => state.batchProduct.entities);
  const batchEntity = useAppSelector(state => state.batch.entity);
  const loading = useAppSelector(state => state.batch.loading);
  const updating = useAppSelector(state => state.batch.updating);
  const updateSuccess = useAppSelector(state => state.batch.updateSuccess);
  const handleClose = () => {
    props.history.push('/batch' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBatchStatuses({}));
    dispatch(getBusinessAssociates({}));
    dispatch(getBatchProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...batchEntity,
      ...values,
      bachStatus: batchStatuses.find(it => it.id.toString() === values.bachStatus.toString()),
      businessAssociate: businessAssociates.find(it => it.id.toString() === values.businessAssociate.toString()),
      batchProduct: batchProducts.find(it => it.id.toString() === values.batchProduct.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...batchEntity,
          bachStatus: batchEntity?.bachStatus?.id,
          businessAssociate: batchEntity?.businessAssociate?.id,
          batchProduct: batchEntity?.batchProduct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.batch.home.createOrEditLabel" data-cy="BatchCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.batch.home.createOrEditLabel">Create or edit a Batch</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="batch-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.batch.name')}
                id="batch-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.batch.description')}
                id="batch-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="batch-bachStatus"
                name="bachStatus"
                data-cy="bachStatus"
                label={translate('managerProductStockApp.batch.bachStatus')}
                type="select"
                required
              >
                <option value="" key="0" />
                {batchStatuses
                  ? batchStatuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="batch-businessAssociate"
                name="businessAssociate"
                data-cy="businessAssociate"
                label={translate('managerProductStockApp.batch.businessAssociate')}
                type="select"
                required
              >
                <option value="" key="0" />
                {businessAssociates
                  ? businessAssociates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="batch-batchProduct"
                name="batchProduct"
                data-cy="batchProduct"
                label={translate('managerProductStockApp.batch.batchProduct')}
                type="select"
                required
              >
                <option value="" key="0" />
                {batchProducts
                  ? batchProducts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/batch" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BatchUpdate;
