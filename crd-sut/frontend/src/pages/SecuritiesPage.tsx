import { useQuery } from '@tanstack/react-query';
import { Alert, Box, Chip, CircularProgress, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { listSecurities } from '../api/client';

export function SecuritiesPage() {
  const { data = [], isLoading, error } = useQuery({ queryKey: ['securities'], queryFn: listSecurities });

  if (isLoading) {
    return <CircularProgress aria-label="Loading securities" />;
  }

  if (error) {
    return <Alert severity="error">Could not load securities.</Alert>;
  }

  return (
    <Box>
      <Typography variant="h5" component="h1" gutterBottom>
        Securities
      </Typography>
      <TableContainer component={Paper} variant="outlined">
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Symbol</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Asset Class</TableCell>
              <TableCell>Flags</TableCell>
              <TableCell align="right">Price</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((security) => (
              <TableRow key={security.id}>
                <TableCell>{security.symbol}</TableCell>
                <TableCell>{security.name}</TableCell>
                <TableCell>{security.assetClass}</TableCell>
                <TableCell>
                  <Box sx={{ display: 'flex', gap: 1 }}>
                    <Chip size="small" label={security.active ? 'ACTIVE' : 'INACTIVE'} color={security.active ? 'success' : 'default'} />
                    {security.restricted && <Chip size="small" label="RESTRICTED" color="warning" />}
                  </Box>
                </TableCell>
                <TableCell align="right">
                  {security.currentPrice.toLocaleString(undefined, { style: 'currency', currency: security.currency })}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
