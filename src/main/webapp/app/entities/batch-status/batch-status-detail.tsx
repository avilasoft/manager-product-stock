import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './batch-status.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BatchStatusDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const batchStatusEntity = useAppSelector(state => state.batchStatus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="batchStatusDetailsHeading">
          <Translate contentKey="managerProductStockApp.batchStatus.detail.title">BatchStatus</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{batchStatusEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.batchStatus.name">Name</Translate>
            </span>
          </dt>
          <dd>{batchStatusEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.batchStatus.description">Description</Translate>
            </span>
          </dt>
          <dd>{batchStatusEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/batch-status" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/batch-status/${batchStatusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BatchStatusDetail;
